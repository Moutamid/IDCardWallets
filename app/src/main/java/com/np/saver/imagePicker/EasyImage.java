package com.np.saver.imagePicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class EasyImage implements Constants {
    private static final String KEY_LAST_CAMERA_PHOTO = "pl.aprilapps.easyphotopicker.last_photo";
    private static final String KEY_LAST_CAMERA_VIDEO = "pl.aprilapps.easyphotopicker.last_video";
    private static final String KEY_PHOTO_URI = "pl.aprilapps.easyphotopicker.photo_uri";
    private static final String KEY_TYPE = "pl.aprilapps.easyphotopicker.type";
    private static final String KEY_VIDEO_URI = "pl.aprilapps.easyphotopicker.video_uri";
    private static final boolean SHOW_GALLERY_IN_CHOOSER = false;

    public interface Callbacks {
        void onCanceled(ImageSource imageSource, int i);

        void onImagePickerError(Exception exception, ImageSource imageSource, int i);

        void onImagesPicked(@NonNull List<File> list, ImageSource imageSource, int i);
    }

    public enum ImageSource {
        GALLERY,
        DOCUMENTS,
        CAMERA_IMAGE,
        CAMERA_VIDEO
    }

    public static boolean willHandleActivityResult(int i, int i2, Intent intent) {
        if (!(i == 32768 || i == RequestCodes.PICK_PICTURE_FROM_GALLERY || i == RequestCodes.TAKE_PICTURE)) {
            if (i != RequestCodes.PICK_PICTURE_FROM_DOCUMENTS) {
                return false;
            }
        }
        return true;
    }

    private static Uri createCameraPictureFile(@NonNull Context context) throws IOException {
        File cameraPicturesLocation = EasyImageFiles.getCameraPicturesLocation(context);
        Uri uriToFile = EasyImageFiles.getUriToFile(context, cameraPicturesLocation);
        SharedPreferences.Editor contexte = PreferenceManager.getDefaultSharedPreferences(context).edit();
        contexte.putString(KEY_PHOTO_URI, uriToFile.toString());
        contexte.putString(KEY_LAST_CAMERA_PHOTO, cameraPicturesLocation.toString());
        contexte.apply();
        return uriToFile;
    }

    private static Uri createCameraVideoFile(@NonNull Context context) throws IOException {
        File cameraVideoLocation = EasyImageFiles.getCameraVideoLocation(context);
        Uri uriToFile = EasyImageFiles.getUriToFile(context, cameraVideoLocation);
        SharedPreferences.Editor wcontext = PreferenceManager.getDefaultSharedPreferences(context).edit();
        wcontext.putString(KEY_VIDEO_URI, uriToFile.toString());
        wcontext.putString(KEY_LAST_CAMERA_VIDEO, cameraVideoLocation.toString());
        wcontext.apply();
        return uriToFile;
    }

    private static Intent createDocumentsIntent(@NonNull Context context, int i) {
        storeType(context, i);
        Intent contexts = new Intent("android.intent.action.GET_CONTENT");
        contexts.setType("image/*");
        return contexts;
    }

    private static Intent createGalleryIntent(@NonNull Context context, int i) {
        storeType(context, i);
        Intent iw = plainGalleryPickerIntent();
        if (VERSION.SDK_INT >= 18) {
            iw.putExtra("android.intent.extra.ALLOW_MULTIPLE", configuration(context).allowsMultiplePickingInGallery());
        }
        return iw;
    }

    private static Intent createCameraForImageIntent(@NonNull Context context, int i) {
        storeType(context, i);
        Intent wi = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            Parcelable createCameraPictureFile = createCameraPictureFile(context);
            grantWritePermission(context, wi, (Uri) createCameraPictureFile);
            wi.putExtra("output", createCameraPictureFile);
        } catch (Exception context2) {
            context2.printStackTrace();
        }
        return wi;
    }

    private static Intent createCameraForVideoIntent(@NonNull Context context, int i) {
        storeType(context, i);
        Intent ei = new Intent("android.media.action.VIDEO_CAPTURE");
        try {
            Parcelable createCameraVideoFile = createCameraVideoFile(context);
            grantWritePermission(context, ei, (Uri) createCameraVideoFile);
            ei.putExtra("output", createCameraVideoFile);
        } catch (Exception context2) {
            context2.printStackTrace();
        }
        return ei;
    }

    @SuppressLint("WrongConstant")
    private static void revokeWritePermission(@NonNull Context context, Uri uri) {
        context.revokeUriPermission(uri, 3);
    }

    @SuppressLint("WrongConstant")
    private static void grantWritePermission(@NonNull Context context, Intent intent, Uri uri) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 65536)) {
            context.grantUriPermission(resolveInfo.activityInfo.packageName, uri, 3);
        }
    }

    private static Intent createChooserIntent(@NonNull Context context, @Nullable String str, int i) throws IOException {
        return createChooserIntent(context, str, false, i);
    }

    private static Intent createChooserIntent(@NonNull Context context, @Nullable String str, boolean z, int i) throws IOException {
        storeType(context, i);
        Object createCameraPictureFile = createCameraPictureFile(context);
        List arrayList = new ArrayList();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            String str2 = resolveInfo.activityInfo.packageName;
            Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
            intent2.setPackage(str2);
            intent2.putExtra("output", (Bundle) createCameraPictureFile);
            grantWritePermission(context, intent2, (Uri) createCameraPictureFile);
            arrayList.add(intent2);
        }
        if (z) {
            Intent wcontext = createGalleryIntent(context, i);
        } else {
            Intent econtext = createDocumentsIntent(context, i);
        }
        Intent rcontext = (Intent) Intent.createChooser(intent, str);
        rcontext.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[arrayList.size()]));
        return rcontext;
    }

    private static void storeType(@NonNull Context context, int i) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_TYPE, i).commit();
    }

    private static int restoreType(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TYPE, 0);
    }

    public static void openChooserWithDocuments(Activity activity, @Nullable String str, int i) {
        try {
            activity.startActivityForResult(createChooserIntent(activity, str, i), 35692);
        } catch (Exception activity2) {
            activity2.printStackTrace();
        }
    }

    public static void openChooserWithDocuments(Fragment fragment, @Nullable String str, int i) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), str, i), 35692);
        } catch (Exception fragment2) {
            fragment2.printStackTrace();
        }
    }

    public static void openChooserWithDocuments(android.app.Fragment fragment, @Nullable String str, int i) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), str, i), 35692);
        } catch (Exception fragment2) {
            fragment2.printStackTrace();
        }
    }

    public static void openChooserWithGallery(Activity activity, @Nullable String str, int i) {
        try {
            activity.startActivityForResult(createChooserIntent(activity, str, true, i), 37740);
        } catch (Exception activity2) {
            activity2.printStackTrace();
        }
    }

    public static void openChooserWithGallery(Fragment fragment, @Nullable String str, int i) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), str, true, i), 37740);
        } catch (Exception fragment2) {
            fragment2.printStackTrace();
        }
    }

    public static void openChooserWithGallery(android.app.Fragment fragment, @Nullable String str, int i) {
        try {
            fragment.startActivityForResult(createChooserIntent(fragment.getActivity(), str, true, i), 37740);
        } catch (Exception fragment2) {
            fragment2.printStackTrace();
        }
    }

    public static void openDocuments(Activity activity, int i) {
        activity.startActivityForResult(createDocumentsIntent(activity, i), RequestCodes.PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openDocuments(Fragment fragment, int i) {
        fragment.startActivityForResult(createDocumentsIntent(fragment.getContext(), i), RequestCodes.PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openDocuments(android.app.Fragment fragment, int i) {
        fragment.startActivityForResult(createDocumentsIntent(fragment.getActivity(), i), RequestCodes.PICK_PICTURE_FROM_DOCUMENTS);
    }

    public static void openGallery(Activity activity, int i) {
        activity.startActivityForResult(createGalleryIntent(activity, i), RequestCodes.PICK_PICTURE_FROM_GALLERY);
    }

    public static void openGallery(Fragment fragment, int i) {
        fragment.startActivityForResult(createGalleryIntent(fragment.getContext(), i), RequestCodes.PICK_PICTURE_FROM_GALLERY);
    }

    public static void openGallery(android.app.Fragment fragment, int i) {
        fragment.startActivityForResult(createGalleryIntent(fragment.getActivity(), i), RequestCodes.PICK_PICTURE_FROM_GALLERY);
    }

    public static void openCameraForImage(Activity activity, int i) {
        activity.startActivityForResult(createCameraForImageIntent(activity, i), RequestCodes.TAKE_PICTURE);
    }

    public static void openCameraForImage(Fragment fragment, int i) {
        fragment.startActivityForResult(createCameraForImageIntent(fragment.getActivity(), i), RequestCodes.TAKE_PICTURE);
    }

    public static void openCameraForImage(android.app.Fragment fragment, int i) {
        fragment.startActivityForResult(createCameraForImageIntent(fragment.getActivity(), i), RequestCodes.TAKE_PICTURE);
    }

    public static void openCameraForVideo(Activity activity, int i) {
        activity.startActivityForResult(createCameraForVideoIntent(activity, i), RequestCodes.CAPTURE_VIDEO);
    }

    public static void openCameraForVideo(Fragment fragment, int i) {
        fragment.startActivityForResult(createCameraForVideoIntent(fragment.getActivity(), i), RequestCodes.CAPTURE_VIDEO);
    }

    public static void openCameraForVideo(android.app.Fragment fragment, int i) {
        fragment.startActivityForResult(createCameraForVideoIntent(fragment.getActivity(), i), RequestCodes.CAPTURE_VIDEO);
    }

    @Nullable
    private static File takenCameraPicture(Context context) throws IOException, URISyntaxException {
        String strCamera = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_PHOTO, null);
        if (strCamera != null && !strCamera.isEmpty()) {
            return new File(strCamera);
        }
        return null;
    }

    @Nullable
    private static File takenCameraVideo(Context context) throws IOException, URISyntaxException {
        String strCameraVideo = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_VIDEO, null);
        if (strCameraVideo != null && !strCameraVideo.isEmpty()) {
            return new File(strCameraVideo);
        }
        return null;
    }

    public static void handleActivityResult(int i, int i2, Intent intent, Activity activity, @NonNull Callbacks callbacks) {
        if (((i & RequestCodes.EASYIMAGE_IDENTIFICATOR) > 0 ? 1 : null) != null) {
            i &= -32769;
            if (i != RequestCodes.PICK_PICTURE_FROM_GALLERY && i != RequestCodes.TAKE_PICTURE && i != RequestCodes.CAPTURE_VIDEO && i != RequestCodes.PICK_PICTURE_FROM_DOCUMENTS) {
                return;
            }
            if (i2 == -1) {
                if (i == RequestCodes.PICK_PICTURE_FROM_DOCUMENTS && isPhoto(intent)) {
                    onPictureReturnedFromDocuments(intent, activity, callbacks);
                } else if (i == RequestCodes.PICK_PICTURE_FROM_GALLERY && isPhoto(intent)) {
                    onPictureReturnedFromGallery(intent, activity, callbacks);
                } else if (i == RequestCodes.TAKE_PICTURE) {
                    onPictureReturnedFromCamera(activity, callbacks);
                } else if (i == RequestCodes.CAPTURE_VIDEO) {
                    onVideoReturnedFromCamera(activity, callbacks);
                } else if (isPhoto(intent)) {
                    onPictureReturnedFromCamera(activity, callbacks);
                } else {
                    onPictureReturnedFromDocuments(intent, activity, callbacks);
                }
            } else if (i == RequestCodes.PICK_PICTURE_FROM_DOCUMENTS) {
                callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity));
            } else if (i == RequestCodes.PICK_PICTURE_FROM_GALLERY) {
                callbacks.onCanceled(ImageSource.GALLERY, restoreType(activity));
            } else {
                callbacks.onCanceled(ImageSource.CAMERA_IMAGE, restoreType(activity));
            }
        }
    }

    @SuppressLint("NewApi")
    private static boolean isPhoto(Intent intent) {
        if (intent != null) {
            if (intent.getData() != null || intent.getClipData() != null) {
                return Boolean.parseBoolean(null);
            }
        }
        return true;
    }

    private static Intent plainGalleryPickerIntent() {
        return new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
    }

    public static boolean canDeviceHandleGallery(@NonNull Context context) {
        return plainGalleryPickerIntent().resolveActivity(context.getPackageManager()) != null ? true : null;
    }

    public static File lastlyTakenButCanceledPhoto(@NonNull Context context) {
        String stt = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_PHOTO, null);
        if (context == null) {
            return null;
        }
        File file = new File(stt);
        if (file != null && file.exists()) {
            return file;
        }
        return null;
    }

    public static File lastlyTakenButCanceledVideo(@NonNull Context context) {
        String str = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LAST_CAMERA_VIDEO, null);
        if (context == null) {
            return null;
        }
        File file = new File(str);
        if (file != null && file.exists()) {
            return file;
        }
        return null;
    }

    private static void onPictureReturnedFromDocuments(Intent intent, Activity activity, @NonNull Callbacks callbacks) {
        try {
            File intentw = EasyImageFiles.pickedExistingPicture(activity, intent.getData());
            callbacks.onImagesPicked(EasyImageFiles.singleFileList(intentw), ImageSource.DOCUMENTS, restoreType(activity));
            if (configuration(activity).shouldCopyPickedImagesToPublicGalleryAppFolder()) {
                EasyImageFiles.copyFilesInSeparateThread(activity, EasyImageFiles.singleFileList(intentw));
            }
        } catch (Exception intent2) {
            intent2.printStackTrace();
            callbacks.onImagePickerError(intent2, ImageSource.DOCUMENTS, restoreType(activity));
        }
    }

    private static void onPictureReturnedFromGallery(Intent intent, Activity activity, @NonNull Callbacks callbacks) {
        try {
            @SuppressLint({"NewApi", "LocalSuppress"}) ClipData clipData = intent.getClipData();
            List arrayList = new ArrayList();
            if (clipData == null) {
                arrayList.add(EasyImageFiles.pickedExistingPicture(activity, intent.getData()));
            } else {
                for (int intents = 0; intents < clipData.getItemCount(); intents++) {
                    arrayList.add(EasyImageFiles.pickedExistingPicture(activity, clipData.getItemAt(intents).getUri()));
                }
            }
            if (configuration(activity).shouldCopyPickedImagesToPublicGalleryAppFolder()) {
                EasyImageFiles.copyFilesInSeparateThread(activity, arrayList);
            }
            callbacks.onImagesPicked(arrayList, ImageSource.GALLERY, restoreType(activity));
        } catch (Exception intent2) {
            intent2.printStackTrace();
            callbacks.onImagePickerError(intent2, ImageSource.GALLERY, restoreType(activity));
        }
    }

    private static void onPictureReturnedFromCamera(Activity activity, @NonNull Callbacks callbacks) {
        try {
            String string = PreferenceManager.getDefaultSharedPreferences(activity).getString(KEY_PHOTO_URI, null);
            if (!TextUtils.isEmpty(string)) {
                revokeWritePermission(activity, Uri.parse(string));
            }
            File takenCameraPicture = takenCameraPicture(activity);
            List arrayList = new ArrayList();
            arrayList.add(takenCameraPicture);
            if (takenCameraPicture == null) {
                callbacks.onImagePickerError(new IllegalStateException("Unable to get the picture returned from camera"), ImageSource.CAMERA_IMAGE, restoreType(activity));
            } else {
                if (configuration(activity).shouldCopyTakenPhotosToPublicGalleryAppFolder()) {
                    EasyImageFiles.copyFilesInSeparateThread(activity, EasyImageFiles.singleFileList(takenCameraPicture));
                }
                callbacks.onImagesPicked(arrayList, ImageSource.CAMERA_IMAGE, restoreType(activity));
            }
            PreferenceManager.getDefaultSharedPreferences(activity).edit().remove(KEY_LAST_CAMERA_PHOTO).remove(KEY_PHOTO_URI).apply();
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.CAMERA_IMAGE, restoreType(activity));
        }
    }

    private static void onVideoReturnedFromCamera(Activity activity, @NonNull Callbacks callbacks) {
        try {
            Object string = PreferenceManager.getDefaultSharedPreferences(activity).getString(KEY_VIDEO_URI, null);
            if (!TextUtils.isEmpty((CharSequence) string)) {
                revokeWritePermission(activity, Uri.parse((String) string));
            }
            File takenCameraVideo = takenCameraVideo(activity);
            List arrayList = new ArrayList();
            arrayList.add(takenCameraVideo);
            if (takenCameraVideo == null) {
                callbacks.onImagePickerError(new IllegalStateException("Unable to get the video returned from camera"), ImageSource.CAMERA_VIDEO, restoreType(activity));
            } else {
                if (configuration(activity).shouldCopyTakenPhotosToPublicGalleryAppFolder()) {
                    EasyImageFiles.copyFilesInSeparateThread(activity, EasyImageFiles.singleFileList(takenCameraVideo));
                }
                callbacks.onImagesPicked(arrayList, ImageSource.CAMERA_VIDEO, restoreType(activity));
            }
            PreferenceManager.getDefaultSharedPreferences(activity).edit().remove(KEY_LAST_CAMERA_VIDEO).remove(KEY_VIDEO_URI).apply();
        } catch (Exception e) {
            e.printStackTrace();
            callbacks.onImagePickerError(e, ImageSource.CAMERA_VIDEO, restoreType(activity));
        }
    }

    public static void clearConfiguration(@NonNull Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(BundleKeys.FOLDER_NAME).remove(BundleKeys.ALLOW_MULTIPLE).remove(BundleKeys.COPY_TAKEN_PHOTOS).remove(BundleKeys.COPY_PICKED_IMAGES).apply();
    }

    public static EasyImageConfiguration configuration(@NonNull Context context) {
        return new EasyImageConfiguration(context);
    }
}

package pro.udeedit.devtools.anarchist.demo.data.actions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_BATTERY_OPTIMIZATION
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CALENDAR
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CAMERA
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CONTACTS_READ
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CONTACTS_WRITE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_EXACT_ALARM
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_LOCATION_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_LOCATION_COARSE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_LOCATION_FINE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MEDIA_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MEDIA_IMAGES
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MICROPHONE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_NOTIFICATIONS
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_PERSONAL_DATA_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_SYSTEM_OVERLAY
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_USAGE_STATS
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_WRITE_SETTINGS
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

/**
 * Centralized dispatcher for all functional "Reward" actions in the Anarchist Demo.
 *
 * Supporting Logic: This class decouples functional system logic from the
 * Permission Registry, following the Strategy pattern to maintain scalability.
 * Every action is guarded by a final library status check.
 */
object PermissionActionExecutor {

    private const val TAG = "PermissionActionExecutor"
    private const val DEMO_CHANNEL_ID = "anarchist_demo_channel"

    /**
     * Performs the appropriate system logic based on the granted permission feature.
     *
     * @param context The context required to trigger system intents or services.
     * @param feature The full [PermissionFeature] object containing status and ID.
     */
    fun performAction(context: Context, feature: PermissionFeature) {
        // Final safety check: ensure the action only runs if the library confirms ALLOWED status
        if (feature.currentStatus != AnarchistStatus.ALLOWED) {
            return
        }

        when (feature.id) {
            // --- 🟢 STANDARD ACTIONS ---
            ID_NOTIFICATIONS -> sendTestNotification(context)

            ID_CAMERA -> openCamera(context)

            ID_LOCATION_FINE, ID_LOCATION_COARSE -> openMapAtLocation(context)

            ID_MICROPHONE -> openVoiceRecorder(context)

            ID_CONTACTS_READ, ID_CONTACTS_WRITE -> openContactsApp(context)

            ID_CALENDAR -> openCalendarApp(context)

            ID_MEDIA_IMAGES -> openGallery(context)


            // --- 🟠 SPECIAL ACTIONS ---
            ID_EXACT_ALARM -> triggerAlarmTest(context)

            ID_SYSTEM_OVERLAY -> verifyOverlayStatus(context)

            ID_WRITE_SETTINGS -> openDisplaySettings(context)

            ID_USAGE_STATS -> openUsageAccessSettings(context)

            ID_BATTERY_OPTIMIZATION -> openBatteryOptimizationSettings(context)


            // --- 🔵 BUNDLE SUCCESS ACTIONS ---
            ID_MEDIA_BUNDLE -> handleBundleSuccess(context, "Audio & Video capabilities unlocked")

            ID_LOCATION_BUNDLE -> handleBundleSuccess(context, "Full Precision Spatial Awareness enabled")

            ID_PERSONAL_DATA_BUNDLE -> handleBundleSuccess(context, "Contacts & Calendar access synchronized")

            else -> {
                Toast.makeText(context, "Action Pending Implementation for ${feature.id}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * Launches the system camera intent.
     */
    private fun openCamera(context: Context) {
        @Suppress("TooGenericExceptionCaught") // Safe for Intent fallback logic
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening camera: ${e.message}")
        }
    }


    /**
     * Opens a map application at specific coordinates.
     */
    private fun openMapAtLocation(context: Context) {
        val mapIntent = Intent(Intent.ACTION_VIEW, "geo:52.5200,13.4050?z=15".toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(mapIntent)
    }


    /**
     * Launches the system voice recorder or audio capture intent.
     */
    @Suppress("TooGenericExceptionCaught")
    private fun openVoiceRecorder(context: Context) {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)

        } catch (e: Exception) {
            Log.d(TAG, "ERROR: ${e.message}")
            Toast.makeText(context, "No audio recording app found", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Opens the system contacts application.
     */
    private fun openContactsApp(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, android.provider.ContactsContract.Contacts.CONTENT_URI).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    /**
     * Opens the system calendar at the current time.
     */
    private fun openCalendarApp(context: Context) {
        val builder = "content://com.android.calendar/time/".toUri().buildUpon()
        val intent = Intent(Intent.ACTION_VIEW).setData(builder.build()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    /**
     * Launches the system gallery to view images.
     */
    @Suppress("TooGenericExceptionCaught")
    private fun openGallery(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            context.startActivity(intent)

        } catch (e: Exception) {
            Log.d(TAG, "ERROR: ${e.message}")
            Toast.makeText(context, "No gallery app found", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Triggers a high-priority notification to verify status.
     */
    private fun sendTestNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DEMO_CHANNEL_ID,
                "Anarchist Demo Actions",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, DEMO_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Anarchist Success! 🏴‍☠️")
            .setContentText("The permission action was performed successfully.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        manager.notify(1, builder.build())
    }


    /**
     * Demonstrates success for settings-based permissions.
     */
    private fun triggerAlarmTest(context: Context) {
        Toast.makeText(context, "Exact Alarm functionality verified.", Toast.LENGTH_SHORT).show()
    }


    /**
     * Opens display settings where system write permissions
     * are often utilized (e.g., Brightness).
     */
    private fun openDisplaySettings(context: Context) {
        val intent = Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    /**
     * Opens the specific Usage Access settings page.
     */
    private fun openUsageAccessSettings(context: Context) {
        val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    /**
     * Redirects to the system battery optimization list.
     */
    private fun openBatteryOptimizationSettings(context: Context) {
        val intent = Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    /**
     * Verification for overlay permissions.
     */
    private fun verifyOverlayStatus(context: Context) {
        Toast.makeText(context, "System Overlay successfully authorized.", Toast.LENGTH_SHORT).show()
    }


    /**
     * Supporting Logic: Generic handler for successful bundle transactions.
     *
     * This function provides a unified way to provide feedback when multiple
     * permissions are granted in a single request.
     *
     * @param context UI Context for the Toast.
     * @param message The specific success message for the feature bundle.
     */
    private fun handleBundleSuccess(context: Context, message: String) {
        Toast.makeText(context, "Bundle Success: $message", Toast.LENGTH_LONG).show()
    }

}

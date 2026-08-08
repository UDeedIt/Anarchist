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
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CAMERA
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_EXACT_ALARM
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_NOTIFICATIONS
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

/**
 * Centralized executor for all functional "Reward" actions in the Anarchist Demo.
 *
 * Supporting Logic: This class decouples the functional system logic from the
 * Permission Registry, following the Strategy pattern to maintain scalability.
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
        if (feature.currentStatus != pro.udeedit.devtools.anarchist.AnarchistStatus.ALLOWED) {
            return
        }

        when (feature.id) {
            ID_NOTIFICATIONS -> sendTestNotification(context)
            ID_CAMERA -> openCamera(context)
            ID_EXACT_ALARM -> triggerAlarmTest(context)
            FeatureIds.ID_LOCATION -> {
                Toast.makeText(context, "Location logic triggered: Reading GPS...", Toast.LENGTH_SHORT).show()
            }
            FeatureIds.ID_MICROPHONE -> {
                Toast.makeText(context, "Microphone logic triggered: Listening...", Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(context, "No action defined for ${feature.id}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera(context: Context) {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(context, "Error opening camera", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error opening camera: ${e.message}")
        }
    }

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
            .setContentText("The permission action was executed successfully.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        manager.notify(1, builder.build())
    }

    private fun triggerAlarmTest(context: Context) {
        Toast.makeText(context, "Exact Alarm test logic triggered", Toast.LENGTH_SHORT).show()
    }
}

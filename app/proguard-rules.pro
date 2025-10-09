# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class org.json.*
-keepclassmembers,includedescriptorclasses class org.json.* { *; }

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-optimizations !method/inlining/*

-keep public class *

#Specifies not to ignore non-public library classes.
-dontskipnonpubliclibraryclasses

#Specifies not to ignore package visible library class members
-dontskipnonpubliclibraryclassmembers

-optimizationpasses 5
#Specifies that the access modifiers of classes and class members may have become broad during processing. This can improve the results of the optimization step.
-allowaccessmodification
#Specifies that interfaces may be merged, even if their implementing classes don't implement all interface methods. This can reduce the size of the output by reducing the total number of classes.
-mergeinterfacesaggressively

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembers,allowobfuscation,includedescriptorclasses class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers enum * {
    @com.google.gson.annotations.SerializedName <fields>;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-dontwarn android.accounts.IAccountManager
-dontwarn android.animation.AnimationHandler
-dontwarn android.annotation.RequiresApi
-dontwarn android.annotation.SystemApi
-dontwarn android.app.ActivityManagerNative
-dontwarn android.app.ActivityTaskManager
-dontwarn android.app.ActivityThread$ActivityClientRecord
-dontwarn android.app.ActivityThread
-dontwarn android.app.AppCompatCallbacks
-dontwarn android.app.ApplicationPackageManager
-dontwarn android.app.IActivityManager
-dontwarn android.app.IAlarmManager
-dontwarn android.app.ILocaleManager
-dontwarn android.app.INotificationManager
-dontwarn android.app.ISearchManager
-dontwarn android.app.IUiModeManager$Stub
-dontwarn android.app.IUiModeManager
-dontwarn android.app.IWallpaperManager
-dontwarn android.app.JobSchedulerImpl
-dontwarn android.app.LoadedApk
-dontwarn android.app.QueuedWork
-dontwarn android.app.ResourcesManager
-dontwarn android.app.StatsManager
-dontwarn android.app.WindowConfiguration
-dontwarn android.app.admin.IDevicePolicyManager
-dontwarn android.app.admin.PolicyState
-dontwarn android.app.ambientcontext.AmbientContextEventRequest
-dontwarn android.app.ambientcontext.AmbientContextManager
-dontwarn android.app.ambientcontext.IAmbientContextManager
-dontwarn android.app.job.IJobScheduler
-dontwarn android.app.role.IRoleManager
-dontwarn android.app.role.RoleControllerManager
-dontwarn android.app.slice.ISliceManager
-dontwarn android.app.time.TimeManager
-dontwarn android.app.time.TimeZoneCapabilities$Builder
-dontwarn android.app.time.TimeZoneCapabilities
-dontwarn android.app.time.TimeZoneConfiguration
-dontwarn android.app.timedetector.ITimeDetectorService
-dontwarn android.app.timezonedetector.ITimeZoneDetectorService
-dontwarn android.app.trust.ITrustManager
-dontwarn android.app.usage.BroadcastResponseStats
-dontwarn android.app.usage.IStorageStatsManager
-dontwarn android.app.usage.IUsageStatsManager
-dontwarn android.app.wearable.IWearableSensingManager
-dontwarn android.app.wearable.WearableSensingManager
-dontwarn android.bluetooth.BluetoothFrameworkInitializer
-dontwarn android.bluetooth.BluetoothLeBroadcast
-dontwarn android.bluetooth.BluetoothPan
-dontwarn android.bluetooth.IBluetooth
-dontwarn android.bluetooth.IBluetoothManager
-dontwarn android.bluetooth.le.DistanceMeasurementManager
-dontwarn android.companion.ICompanionDeviceManager
-dontwarn android.companion.virtual.IVirtualDeviceManager
-dontwarn android.companion.virtual.VirtualDeviceManager$VirtualDevice
-dontwarn android.companion.virtual.sensor.VirtualSensor
-dontwarn android.compat.Compatibility
-dontwarn android.content.IClipboard
-dontwarn android.content.IRestrictionsManager
-dontwarn android.content.integrity.AppIntegrityManager
-dontwarn android.content.integrity.IAppIntegrityManager
-dontwarn android.content.pm.ICrossProfileApps
-dontwarn android.content.pm.ILauncherApps
-dontwarn android.content.pm.IShortcutService
-dontwarn android.content.pm.PackageParser$Callback
-dontwarn android.content.pm.PackageParser$Component
-dontwarn android.content.pm.PackageParser$IntentInfo
-dontwarn android.content.pm.PackageParser$Package
-dontwarn android.content.pm.PackageParser$PermissionGroup
-dontwarn android.content.pm.PackageParser
-dontwarn android.content.pm.SuspendDialogInfo
-dontwarn android.content.pm.UserInfo
-dontwarn android.content.pm.pkg.FrameworkPackageUserState
-dontwarn android.content.res.ApkAssets
-dontwarn android.content.res.CompatibilityInfo
-dontwarn android.content.res.ResourcesImpl
-dontwarn android.content.rollback.IRollbackManager
-dontwarn android.credentials.ICredentialManager
-dontwarn android.database.sqlite.SQLiteConnection
-dontwarn android.graphics.BaseCanvas
-dontwarn android.graphics.BaseRecordingCanvas
-dontwarn android.graphics.CanvasProperty
-dontwarn android.graphics.FontFamily
-dontwarn android.graphics.FrameInfo
-dontwarn android.graphics.HardwareRendererObserver
-dontwarn android.graphics.TableMaskFilter
-dontwarn android.graphics.animation.NativeInterpolatorFactory
-dontwarn android.graphics.animation.RenderNodeAnimator
-dontwarn android.graphics.fonts.FontFileUtil
-dontwarn android.hardware.ISensorPrivacyManager
-dontwarn android.hardware.biometrics.CryptoObject
-dontwarn android.hardware.biometrics.IAuthService
-dontwarn android.hardware.biometrics.IBiometricService
-dontwarn android.hardware.camera2.impl.CameraCaptureSessionImpl
-dontwarn android.hardware.camera2.impl.CameraDeviceImpl
-dontwarn android.hardware.camera2.impl.CameraMetadataNative
-dontwarn android.hardware.display.BrightnessChangeEvent$Builder
-dontwarn android.hardware.display.DisplayManagerGlobal
-dontwarn android.hardware.display.IColorDisplayManager
-dontwarn android.hardware.display.IDisplayManagerCallback
-dontwarn android.hardware.fingerprint.IFingerprintService
-dontwarn android.hardware.input.IInputManager
-dontwarn android.hardware.input.InputManagerGlobal
-dontwarn android.hardware.input.VirtualKeyboard
-dontwarn android.hardware.input.VirtualMouse
-dontwarn android.hardware.input.VirtualTouchscreen
-dontwarn android.hardware.location.ContextHubClient
-dontwarn android.hardware.location.ContextHubInfo
-dontwarn android.hardware.location.ContextHubManager
-dontwarn android.hardware.location.IContextHubService
-dontwarn android.hardware.soundtrigger.SoundTrigger$ModuleProperties
-dontwarn android.hardware.usb.IUsbManager
-dontwarn android.location.ICountryDetector
-dontwarn android.location.ILocationManager
-dontwarn android.media.AudioSystem
-dontwarn android.media.IAudioService
-dontwarn android.media.IMediaRouterService
-dontwarn android.media.audiopolicy.AudioProductStrategy
-dontwarn android.media.session.ISessionManager
-dontwarn android.net.IConnectivityManager
-dontwarn android.net.IIpSecService
-dontwarn android.net.INetworkPolicyManager
-dontwarn android.net.INetworkScoreService
-dontwarn android.net.ITetheringConnector
-dontwarn android.net.IVpnManager
-dontwarn android.net.NetworkScoreManager
-dontwarn android.net.nsd.INsdManager
-dontwarn android.net.vcn.IVcnManagementService
-dontwarn android.net.wifi.IWifiManager
-dontwarn android.net.wifi.IWifiScanner
-dontwarn android.net.wifi.WifiScanner
-dontwarn android.net.wifi.aware.IWifiAwareManager
-dontwarn android.net.wifi.p2p.IWifiP2pManager
-dontwarn android.net.wifi.rtt.IWifiRttManager
-dontwarn android.nfc.INfcAdapter
-dontwarn android.nfc.INfcCardEmulation
-dontwarn android.nfc.NfcFrameworkInitializer
-dontwarn android.nfc.NfcServiceManager
-dontwarn android.os.BluetoothServiceManager
-dontwarn android.os.HidlSupport
-dontwarn android.os.IBatteryPropertiesRegistrar
-dontwarn android.os.IDumpstate
-dontwarn android.os.IPowerManager
-dontwarn android.os.IThermalService
-dontwarn android.os.IUserManager
-dontwarn android.os.IncidentManager
-dontwarn android.os.PowerManager$LowPowerStandbyPortsLock
-dontwarn android.os.ServiceManager
-dontwarn android.os.SystemProperties
-dontwarn android.os.SystemVibrator
-dontwarn android.os.TelephonyServiceManager
-dontwarn android.os.storage.IStorageManager
-dontwarn android.permission.ILegacyPermissionManager
-dontwarn android.permission.IPermissionManager
-dontwarn android.permission.PermissionControllerManager
-dontwarn android.provider.DeviceConfig
-dontwarn android.provider.Settings$Config
-dontwarn android.safetycenter.ISafetyCenterManager
-dontwarn android.safetycenter.SafetyCenterManager
-dontwarn android.se.omapi.ISecureElementService$Stub
-dontwarn android.security.IFileIntegrityService
-dontwarn android.service.textclassifier.TextClassifierService
-dontwarn android.service.voice.AlwaysOnHotwordDetector
-dontwarn android.speech.IRecognitionServiceManager
-dontwarn android.telecom.InCallAdapter
-dontwarn android.telecom.Phone
-dontwarn android.telephony.NetworkRegistrationInfo$Builder
-dontwarn android.telephony.TelephonyFrameworkInitializer
-dontwarn android.text.Hyphenator
-dontwarn android.text.MeasuredParagraph
-dontwarn android.util.IntArray
-dontwarn android.util.MergedConfiguration
-dontwarn android.util.PathParser
-dontwarn android.uwb.AdapterStateListener
-dontwarn android.uwb.IUwbAdapter
-dontwarn android.uwb.RangingSession
-dontwarn android.uwb.UwbManager$AdapterStateCallback
-dontwarn android.uwb.UwbManager
-dontwarn android.view.DisplayCutout$ParcelableWrapper
-dontwarn android.view.DisplayEventReceiver
-dontwarn android.view.DisplayInfo
-dontwarn android.view.IWindow
-dontwarn android.view.IWindowManager
-dontwarn android.view.IWindowSession
-dontwarn android.view.InputDevice$Builder
-dontwarn android.view.InputEventReceiver
-dontwarn android.view.InsetsAnimationThread
-dontwarn android.view.InsetsSource
-dontwarn android.view.InsetsSourceControl$Array
-dontwarn android.view.InsetsState
-dontwarn android.view.RenderNodeAnimator
-dontwarn android.view.ThreadedRenderer
-dontwarn android.view.ViewRootImpl
-dontwarn android.view.WindowManagerGlobal
-dontwarn android.view.WindowManagerImpl
-dontwarn android.view.WindowRelayoutResult
-dontwarn android.view.autofill.AutofillManager$AutofillClient
-dontwarn android.view.contentcapture.IContentCaptureManager
-dontwarn android.view.translation.ITranslationManager
-dontwarn android.widget.RemoteViewsAdapter
-dontwarn android.widget.SpellChecker
-dontwarn android.window.ActivityWindowInfo
-dontwarn android.window.ClientWindowFrames
-dontwarn android.window.IOnBackInvokedCallback
-dontwarn android.window.OnBackInvokedCallbackInfo
-dontwarn android.window.WindowOnBackInvokedDispatcher
-dontwarn com.android.extensions.xr.XrExtensionResult
-dontwarn com.android.extensions.xr.XrExtensions
-dontwarn com.android.extensions.xr.function.Consumer
-dontwarn com.android.extensions.xr.node.InputEvent$HitInfo
-dontwarn com.android.extensions.xr.node.InputEvent
-dontwarn com.android.extensions.xr.node.Mat4f
-dontwarn com.android.extensions.xr.node.Node
-dontwarn com.android.extensions.xr.node.NodeTransaction
-dontwarn com.android.extensions.xr.node.NodeTransform
-dontwarn com.android.extensions.xr.node.Vec3
-dontwarn com.android.extensions.xr.splitengine.BufferHandle
-dontwarn com.android.extensions.xr.splitengine.MessageGroupCallback
-dontwarn com.android.extensions.xr.splitengine.RequestCallback
-dontwarn com.android.extensions.xr.splitengine.SystemRendererConnection
-dontwarn com.android.extensions.xr.subspace.Subspace
-dontwarn com.android.i18n.timezone.TimeZoneFinder
-dontwarn com.android.i18n.timezone.internal.BufferIterator
-dontwarn com.android.i18n.timezone.internal.MemoryMappedFile
-dontwarn com.android.internal.annotations.GuardedBy
-dontwarn com.android.internal.app.AlertController
-dontwarn com.android.internal.app.IAppOpsService$Stub
-dontwarn com.android.internal.app.IAppOpsService
-dontwarn com.android.internal.app.IBatteryStats
-dontwarn com.android.internal.app.ISoundTriggerService
-dontwarn com.android.internal.app.IVoiceInteractionManagerService
-dontwarn com.android.internal.appwidget.IAppWidgetService
-dontwarn com.android.internal.compat.ChangeReporter
-dontwarn com.android.internal.compat.IPlatformCompat
-dontwarn com.android.internal.content.om.OverlayConfig
-dontwarn com.android.internal.inputmethod.RemoteAccessibilityInputConnection
-dontwarn com.android.internal.os.BackgroundThread
-dontwarn com.android.internal.os.IDropBoxManagerService
-dontwarn com.android.internal.statusbar.IStatusBar
-dontwarn com.android.internal.telephony.ITelephony
-dontwarn com.android.internal.telephony.ITelephonyRegistry
-dontwarn com.android.internal.util.AnnotationValidations
-dontwarn com.android.internal.util.VirtualRefBasePtr
-dontwarn com.android.internal.util.XmlUtils
-dontwarn com.android.internal.view.IInputMethodManager
-dontwarn com.google.auto.service.AutoService
-dontwarn dalvik.system.CloseGuard
-dontwarn dalvik.system.SocketTagger
-dontwarn dalvik.system.VMRuntime
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn java.awt.geom.Path2D$Double
-dontwarn java.awt.geom.Path2D
-dontwarn java.awt.geom.PathIterator
-dontwarn java.awt.geom.RectangularShape
-dontwarn java.beans.BeanInfo
-dontwarn java.beans.FeatureDescriptor
-dontwarn java.beans.IntrospectionException
-dontwarn java.beans.Introspector
-dontwarn java.beans.PropertyDescriptor
-dontwarn java.lang.instrument.ClassDefinition
-dontwarn java.lang.instrument.IllegalClassFormatException
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn java.lang.invoke.SwitchPoint
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn java.lang.reflect.AnnotatedType
-dontwarn java.util.ServiceLoader$Provider
-dontwarn javax.xml.stream.XMLInputFactory
-dontwarn javax.xml.stream.XMLStreamException
-dontwarn javax.xml.stream.XMLStreamReader
-dontwarn libcore.content.type.MimeMap
-dontwarn libcore.icu.DateIntervalFormat
-dontwarn libcore.icu.ICU
-dontwarn libcore.icu.LocaleData
-dontwarn libcore.io.BufferIterator
-dontwarn libcore.io.IoUtils
-dontwarn libcore.io.Linux
-dontwarn libcore.io.MemoryMappedFile
-dontwarn libcore.net.InetAddressUtils
-dontwarn libcore.util.NativeAllocationRegistry
-dontwarn net.bytebuddy.jar.asm.commons.ModuleHashesAttribute
-dontwarn org.mockito.internal.creation.bytebuddy.MockMethodDispatcher
-dontwarn org.newsclub.net.unix.AFUNIXSocket



-keep class com.nb.coininfo.data.models.** { *; }
-keep class com.nb.coininfo.data.models.* { *; }
-keep class com.nb.coininfo.data.models**
-keep class com.nb.coininfo.data.models.**
-keepclassmembers class com.nb.coininfo.data.models** {*;}
-keepclassmembers class com.nb.coininfo.data.models.** {*;}

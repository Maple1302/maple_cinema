import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.atomic.AtomicLong
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}


object NetworkUsageInterceptor : Interceptor {
    var receivedBytes = AtomicLong(0)
    var sentBytes = AtomicLong(0)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Tính toán dung lượng request và response
        sentBytes.addAndGet(request.body?.contentLength() ?: 0)
        receivedBytes.addAndGet(response.body?.contentLength() ?: 0)

        return response
    }
}
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(NetworkUsageInterceptor)
    .build()


@Composable
fun NetworkUsageScreen() {
    val received = remember { NetworkUsageInterceptor.receivedBytes.get() / 1024 }
    val sent = remember { NetworkUsageInterceptor.sentBytes.get() / 1024 }

    Column {
        Text("Data Received: $received KB")
        Text("Data Sent: $sent KB")
        Text("Total: ${received + sent} KB")
    }
}
package video.pal

import video.pal.api.SessionApi
import video.pal.api.http.SessionHttpApi
import video.pal.api.models.Session
import video.pal.api.models.SessionDtoReq
import video.pal.api.storage.SessionStorageApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import retrofit2.Response

class PalPluginUnitTest {

    var palPlugin: PalPlugin? = null

    var mockSessionHttpApi: SessionHttpApi = mock()

    var mockSessionStorageApi: SessionStorageApi = mock()

    @Before
    fun initPal() {
        val sessionApi = SessionApi(
            mockSessionHttpApi,
            mockSessionStorageApi
        )
        palPlugin = PalPlugin.instance
    }

    //  initialize plugin, session api returns a session => session api hasSession returns true")
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun init_success() = runTest(UnconfinedTestDispatcher()){
//        val session = Session("3802830238024A")
//        val sessionResponse =  Response.success(session)
//        val sessionDtoReq = SessionDtoReq("android", "android")
//        launch {
//            Mockito
//                .`when`(mockSessionHttpApi.create(sessionDtoReq))
//                .thenReturn(sessionResponse)
//            Mockito.`when`(mockSessionStorageApi.get())
//                .thenReturn(null)
//            palPlugin!!.initialize()
//            Assert.assertTrue(palPlugin!!.hasInit())
//        }
//    }
}
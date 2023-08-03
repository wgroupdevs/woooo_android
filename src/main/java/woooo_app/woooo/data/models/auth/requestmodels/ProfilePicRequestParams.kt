package woooo_app.woooo.data.models.auth.requestmodels

import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfilePicRequestParams(
   var accountUniqueId: RequestBody,
   var imageFile: MultipartBody.Part
)
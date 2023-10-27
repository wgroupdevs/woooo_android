package eu.siacs.conversations.http.model.requestmodels

import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfilePicRequestParams(
   var accountUniqueId: RequestBody,
   var imageFile: MultipartBody.Part
)
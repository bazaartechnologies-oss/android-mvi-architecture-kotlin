package com.tech.bazaar.template.base

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FireBaseGet @Inject constructor() {

    lateinit var db: FirebaseFirestore

    fun getForcedVersions(
        collection: String,
        document: String,
        success: (versions: ArrayList<Long>?, minVersion: Long?) -> Unit
    ) {
        db = FirebaseFirestore.getInstance()
        var versions: ArrayList<Long>?
        var minVersion: Long?
        val docRef = db.collection(collection).document(document)
        docRef.get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    versions = doc.get(VERSIONS) as? ArrayList<Long>
                    minVersion = doc.getLong(MIN_VERSION)
                    success.invoke(versions, minVersion)
                } else {
                    success.invoke(null, null)
                }
            }
            .addOnFailureListener { exception ->
                success.invoke(null, null)
            }
    }

    companion object {
        const val NOT_PRESENT = "NOT PRESENT"
        const val VERSIONS = "versions"
        const val MIN_VERSION = "min_version"
    }

}


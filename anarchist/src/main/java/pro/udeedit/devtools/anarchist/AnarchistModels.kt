package pro.udeedit.devtools.anarchist

/**
 * Represents the current state of a system permission.
 */
enum class AnarchistStatus {
    ALLOWED,            // Permission granted
    DENIED,             // Permission denied, but can be requested again
    DENIED_PERMANENTLY  // Permission denied with "Don't ask again" checked
}

/**
 * Data container for the results of a multi-permission request.
 */
class AnarchistResult {
    /** Map of individual permissions and their specific status. */
    var details: HashMap<String, AnarchistStatus> = hashMapOf()

    /** The consolidated status of the entire request. */
    var finalStatus: AnarchistStatus = AnarchistStatus.DENIED
}

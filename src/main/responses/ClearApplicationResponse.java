package responses;

import java.util.Objects;

public class ClearApplicationResponse {

    /**
     * a message indicating why the request to remove all data from the database was unsuccessful
     */
    private String message;

    /**
     * Create a new response indicating that all information was successfully removed from the database
     */
    public ClearApplicationResponse() {

    }

    /**
     * Create a new response indicating that all information was not successfully removed from the database
     * @param message a message indicating why the request to remove all data from the database was unsuccessful
     */
    public ClearApplicationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != ClearApplicationResponse.class) return false;
        ClearApplicationResponse other = (ClearApplicationResponse) o;
        return Objects.equals(this.message, other.message);
    }

}

document.addEventListener("DOMContentLoaded", function () {
    const userId = localStorage.getItem("userId");

    if (!userId) {
        alert("Unauthorized access! Please log in.");
        window.location.href = "/login.html";
        return;
    }

    // Fetch user details from localStorage
    document.getElementById("name").value = localStorage.getItem("userName") || "";
    document.getElementById("email").value = localStorage.getItem("userEmail") || "";
    document.getElementById("phone").value = localStorage.getItem("userPhone") || "";
    document.getElementById("department").value = localStorage.getItem("userDepartment") || "";
    document.getElementById("password").value = localStorage.getItem("userPassword") || "";
});

//function to update profile
function updateProfile() {
    const userId = localStorage.getItem("userId");

    if (!userId) {
        alert("Unauthorized access!");
        return;
    }

    // Get updated values from the form
    const updatedUser = {
        id: userId,
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        department: document.getElementById("department").value.trim(),
        password: document.getElementById("password").value.trim()
    };

    // Update localStorage with new values
    localStorage.setItem("userName", updatedUser.name);
    localStorage.setItem("userEmail", updatedUser.email);
    localStorage.setItem("userPhone", updatedUser.phone);
    localStorage.setItem("userDepartment", updatedUser.department);
    localStorage.setItem("userPassword", updatedUser.password);

    // Send updated data to backend
    fetch(`http://localhost:8080/auth/update?id=${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedUser)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.text();
    })
    .then(response => {
        alert("Profile updated successfully!");
        window.location.href = "/employee_dashboard.html"; // Redirect after update
    })
    .catch(error => {
        console.error("Error updating profile:", error);
        alert("Update failed: " + error.message);
    });
}

let employeeId = localStorage.getItem("id");

function fetchEmployeeDetails() {
    if (!employeeId) {
        alert("No employee ID found!");
        window.location.href = "/edit_employee.html";
        return;
    }

    fetch(`http://localhost:8080/admin/employees/${employeeId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Employee not found");
            }
            return response.json();
        })
        .then(employee => {
            document.getElementById("name").value = employee.name;
            document.getElementById("email").value = employee.email;
            document.getElementById("phone").value = employee.phone;
            document.getElementById("department").value = employee.department;
            document.getElementById("password").value = employee.password;
        })
        .catch(error => {
            alert("Error fetching employee details: " + error.message);
            window.location.href = "/edit_employee.html";
        });
}

// Ensure function runs when page loads
window.onload = fetchEmployeeDetails;

function editEmployee() {
    const updatedEmployee = {
        id: Number(employeeId),
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        department: document.getElementById("department").value,
        password: document.getElementById("password").value
    };

    fetch(`http://localhost:8080/admin/employees/${employeeId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedEmployee)
    })
    .then(response => {
        if (response.ok) {
            alert("Employee updated successfully!");
            window.location.href = "/manage_employee.html";
        } else {
            alert("Error updating employee");
        }
    })
    .catch(error => console.error("Error updating employee:", error));
}


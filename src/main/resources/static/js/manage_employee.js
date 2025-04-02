document.addEventListener("DOMContentLoaded", function() {
    fetchEmployees();
});

// Fetch employees and display in the table
function fetchEmployees() {
    fetch("http://localhost:8080/admin/employees")
        .then(response => response.json())
        .then(data => {
            const employeeTableBody = document.getElementById("employeeTableBody");
            employeeTableBody.innerHTML = ""; // Clear existing content

            data.forEach((employee, index) => {
                let row = `<tr>
                    <td>${employee.id}</td>
                    <td>${employee.name}</td>
                    <td>${employee.email}</td>
                    <td>${employee.phone}</td>
                    <td>${employee.department}</td>
                    <td>
                        <button class="edit-btn" onclick="redirectToEdit(${employee.id})">Edit</button>
                        <button class="delete-btn" onclick="deleteEmployee(${employee.id})">Delete</button>
                        <button class="upgrade-btn" onclick="upgradeRole(${employee.id})">Upgrade Role</button>
                    </td>
                </tr>`;
                employeeTableBody.innerHTML += row;
            });
        })
        .catch(error => console.error("Error fetching employees:", error));
}

// Edit Employee
function redirectToEdit(id) {
    localStorage.setItem("id",id);
    window.location.href = `/edit_employee.html?id=${id}`;
}


// Delete Employee
function deleteEmployee(id) {
    if (confirm("Are you sure you want to delete this employee?")) {
        fetch(`http://localhost:8080/api/employees/${id}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                alert("Employee deleted successfully.");
                fetchEmployees(); // Refresh employee list
            } else {
                alert("Failed to delete employee.");
            }
        })
        .catch(error => console.error("Error deleting employee:", error));
    }
}

//function for upgrade role
function upgradeRole(id) {
    fetch(`/admin/employees/${id}/role?newRole=ADMIN`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" }
    })
    .then(response => {
        if (response.ok) {
            alert("Role upgraded successfully!");
            location.reload();
        } else {
            alert("Failed to upgrade role.");
        }
    });
}

//function for logout
function logout() {
    localStorage.clear();
    alert("Logged out successfully!");
    window.location.href = "/login.html";
}


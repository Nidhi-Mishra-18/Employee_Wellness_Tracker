document.addEventListener("DOMContentLoaded", fetchWellnessReport);

//fetch wellness report from the database using api
function fetchWellnessReport() {
    fetch("http://localhost:8080/api/reports/wellness-report")
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("reportBody");
            tableBody.innerHTML = "";
            data.forEach(report => {
                const row = `<tr>
                    <td>${report.employeeId}</td>
                    <td>${report.name}</td>
                    <td>${report.title}</td>
                    <td>${report.score ? report.score.toFixed(2) : "N/A"}</td>
                    <td>
                        <button onclick="exportEmployeeReport(${report.employeeId})">
                            Export CSV
                        </button>    
                    </td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error("Error fetching reports:", error));
}
//export all wellness reports
function exportCSV() {
    window.location.href = "http://localhost:8080/api/reports/export/csv"; // Fixed API URL
}
//export wellness report of each employee
function exportEmployeeReport(employeeId) {
    window.location.href = `http://localhost:8080/api/reports/export/csv/${employeeId}`; // Fixed API URL
}
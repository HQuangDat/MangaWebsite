document.addEventListener('DOMContentLoaded', function () {
    // Gọi API để lấy dữ liệu từ server
    // Gọi API để lấy dữ liệu từ server
    fetch('/admin', {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const dates = Object.keys(data);
            const counts = Object.values(data);

            // Tạo biểu đồ
            const ctx = document.getElementById('chart').getContext('2d');
            const chart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: dates,
                    datasets: [{
                        label: 'Số truyện đăng',
                        data: counts,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        });


    // Hàm để đếm số lượng theo ngày
    function countByDate(arr) {
        return arr.reduce((acc, date) => {
            acc[date] = (acc[date] || 0) + 1;
            return acc;
        }, {});
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    if (token) {
        fetch('/api/validateToken?token=' + token)
            .then(response => response.json())
            .then(data => {
                if (data.status === 'valid') {
                    document.getElementById('resetForm').style.display = 'block';
                } else {
                    document.getElementById('error').innerText = 'Invalid or expired token.';
                }
            });
    }
});
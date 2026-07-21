<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Secure MVC login portal for Sunrise Dental Clinic Appointment and Patient Management System.">
    <title>Login - Sunrise Dental Clinic Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-body">

    <!-- Decorative Blurred Background Elements -->
    <div class="bg-decor decor-1"></div>
    <div class="bg-decor decor-2"></div>

    <main class="login-card">
        <div class="login-header">
            <div class="login-logo">
                <span class="logo-icon">🦷</span>
                <span class="logo-text">Sunrise Dental</span>
            </div>
            <h1 class="login-title">Staff Portal (MVC)</h1>
            <p class="login-subtitle">Please enter your credentials to login</p>
        </div>

        <!-- Alert Notification Box -->
        <div id="login-alert" class="alert alert-error" style="display: none;">
            <span>⚠️</span>
            <span id="login-alert-text">Invalid credentials</span>
        </div>

        <form id="login-form" onsubmit="handleLogin(event)">
            <div class="form-grid" style="display: flex; flex-direction: column; gap: 1.25rem;">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="e.g. admin" required autocomplete="username">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="••••••••" required autocomplete="current-password">
                </div>

                <button type="submit" id="btn-login" class="btn btn-primary" style="width: 100%; margin-top: 1rem;">
                    Secure Login
                </button>
            </div>
        </form>
    </main>

    <script>
        function handleLogin(event) {
            event.preventDefault();
            const alertBox = document.getElementById('login-alert');
            const alertText = document.getElementById('login-alert-text');
            const loginBtn = document.getElementById('btn-login');

            alertBox.style.display = 'none';
            loginBtn.innerText = 'Authenticating...';
            loginBtn.disabled = true;

            const usernameInput = document.getElementById('username').value;
            const passwordInput = document.getElementById('password').value;

            const params = new URLSearchParams();
            params.append('username', usernameInput);
            params.append('password', passwordInput);

            fetch('api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return response.json().then(err => { throw new Error(err.error || 'Authentication failed'); });
                }
            })
            .then(data => {
                window.location.href = 'dashboard';
            })
            .catch(err => {
                alertBox.style.display = 'flex';
                alertText.innerText = err.message;
                loginBtn.innerText = 'Secure Login';
                loginBtn.disabled = false;
            });
        }
    </script>
</body>
</html>

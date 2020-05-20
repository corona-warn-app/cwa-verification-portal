<!DOCTYPE html>
<html lang="de" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" media="screen" href="${url.resourcesPath}/login.css" />
    <title>Verification Portal</title>	
  </head>
  <body>
	<div class="header">
		<img class="header-logo" src="${url.resourcesPath}/img/telekom_web_logo.png">
		<img class="header-text" src="${url.resourcesPath}/img/life_is_for_sharing.png">
	</div>
	<div class="footer">
		<p class="footer-font footer-copyright">© Telekom Deutschland GmbH</p>
		<p class="footer-font footer-imprint">Imprint</p>
		<img class="footer-dp-logo" src="${url.resourcesPath}/img/data_protect.png">
		<p class="footer-font footer-dp">Data Protection</p>
	</div>
	<table class="c19-logo">
		<tr>
			<td style="width: 1px;"><img src="${url.resourcesPath}/img/c-19_logo.png"></td>
			<td>Verification Portal</td>
		</tr>
	</table>
	<div class="login-text">Please enter your password.</div>
	<table class="login-cwa-form">
		<tr>
			<td><div class="login-text">Bitte loggen Sie sich mit Ihrem Benutzernamen und Paswwort ein.</div></td>
		</tr>
		<tr>
			<td>
				<form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
					<input class="login-user" type="text" placeholder="Username" name="username" required>
					<input class="login-password" type="password" placeholder="Passwort" name="password" required>
					<input class="login-button" type="submit" value="Login">
				</form>
			</td>
		</tr>
	</table>
 </body>
</html>
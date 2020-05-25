<!DOCTYPE html>
<html lang="de" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" media="screen" href="${url.resourcesPath}/teletan.css" />
    <title>TeleTAN Portal</title>
	<link rel="icon" href="${url.resourcesPath}/img/c-19_logo.png">
  </head>
  <body>
	<div class="header">
		<img class="header-logo" src="${url.resourcesPath}/img/telekom_web_logo.png">
		<img class="header-text" src="${url.resourcesPath}/img/life_is_for_sharing.png">
	</div>
	<div class="footer">
		<p class="footer-font footer-copyright">© Telekom Deutschland GmbH</p>
		<p class="footer-font footer-imprint">Impressum</p>
		<img class="footer-dp-logo" src="${url.resourcesPath}/img/data_protect.png">
		<p class="footer-font footer-dp">Datenschutz</p>
	</div>
	<table class="c19-logo">
		<tr>
			<td style="width: 1px;"><img src="${url.resourcesPath}/img/c-19_logo.png"></td>
			<td>TeleTAN Portal</td>
		</tr>
	</table>
	
	<!-- BEGIN page specific content  -->
	<div class="text-big" style="top: 210px;">Anmeldung</div>
	<div class="text" style="top: 310px;">Bitte wählen Sie ein neues Passwort</div>
	<form id="kc-passwd-update-form" action="${url.loginAction}" method="post">
        <input type="text" name="username" value="${username}" autocomplete="username" readonly="readonly" style="display:none;"/>
		<input class="input" style="top: 360px;" type="password" placeholder="Passwort" name="password-new" required>   
		<input class="input" style="top: 425px;" type="password" placeholder="Passwort bestätigen" name="password-confirm" required>
        <input class="button" style="top: 500px;" type="submit" value="Abschicken">
    </form>
	<!-- END page specific content  -->
 </body>
</html>
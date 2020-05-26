<!DOCTYPE html>
<html lang="de" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" media="screen" href="${url.resourcesPath}/teletan.css"/>
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
<div class="text-bold" style="top: 290px;">Hier können Sie für den Patienten die in der Corona Warn App benötigte
  TeleTAN anfordern.
</div>
<div class="text" style="top: 340px;">Bitte melden Sie sich mit Ihrem Benutzernamen und Passwort an.
  Halten Sie Ihr mobiles Endgerät bereit, auf dem Sie die
  FreeOTP Authenticator App installiert haben.
</div>
<form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
  <input class="input" style="top: 420px;" type="text" placeholder="Benutzername" name="username" required>
  <input class="input" style="top: 485px;" type="password" placeholder="Passwort" name="password" required>
  <input class="button" style="top: 560px;" type="submit" value="Anmelden">
</form>
<!-- END page specific content  -->
</body>
</html>

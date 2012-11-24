
var clientId = '${clientId}';
var apiKey = '${apiKey}';
var scopes = [
"https://www.googleapis.com/auth/plus.me", 
"https://www.googleapis.com/auth/userinfo.email", 
"https://www.googleapis.com/auth/userinfo.profile"
];
    
function clickAbout() {
    console.log("clickAbout");
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-about").show();        
}

function clickHome() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-landing").show();
}        

function clickContact() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-contact").show();
}

function startDocument() {
    console.log("startDocument");
    if (!redirectDocument()) {
        initDocument();
    }
}

function initDocument() {
    console.log("initDocument");
    $(".croc-home-clickable").click(clickHome);
    $(".croc-about-clickable").click(clickAbout);
    $(".croc-contact-clickable").click(clickContact);
    $('.croc-logout-clickable').click(clickLogout);
    $('.croc-login-clickable').click(clickLogin);
    $('#croc-account-genKey').click(clickGenKey);
    $('#croc-account-signCert').click(clickSignCert);
    $('#croc-account-resetOtp').click(clickResetOtp);
    $("#croc-genkey-form").submit(submitGenKey);
    notify("Welcome");
//showReadyAuth();
}

function redirectDocument() {
    console.log("redicrectDocument " + window.location.protocol);
    if (window.location.protocol != "https:") {
        var host = location.host;
        var index = location.host.indexOf(':');
        if (index > 0) {
            host = location.host.substring(0, index) + ':8443';
        }
        window.location = "https://" + host + location.pathname + location.search + location.hash;
        console.log(window.location);
        return true;
    }
    return false;
}

function notify(message) {
    console.log("notify " + message);
}

function startClient() {
    gapi.client.setApiKey(apiKey);
    window.setTimeout(checkAuth, 1);
}

function checkAuth() {
    gapi.auth.authorize({
        client_id: clientId, 
        scope: scopes, 
        immediate: true
    }, processAuthResult);
}

function processAuthResult(authResult) {
    if (authResult && !authResult.error) {
        showBusyAuth();
        login(authResult.access_token);
    } else {
        showReadyAuth();
        console.log("login required");
    }
}

function showBusyAuth() {
    $('.croc-login-clickable').hide();
    $('.croc-login-viewable').hide();
    $('.croc-logout-clickable').hide();
    $('.croc-loggedin-viewable').hide();          
}

function login(accessToken) {
    console.log(accessToken);
    $.ajax({ 
        type: 'POST',                
        url: '/login',
        data: 'accessToken=' + accessToken,
        success: processLogin,
        error: processLoginError
    });
}

function processLoginError() {
    console.log("login error");
    showReadyAuth();
}

function processLogin(res) {
    console.log("login response received")
    if (res.email != null) {
        notify('Welcome, ' + res.name);
        $('#croc-username-text').text(res.email);
        $('#croc-user-picture').attr('src', res.picture);            
        $('#croc-loggedin-qr-img').attr('src', res.qr);
        $('#croc-loggedin-title').text("Welcome, " + res.name);
        $('#croc-totp-text').text(res.totpSecret);
        $('#croc-totp-url').text(res.totpUrl);
        showLoggedIn();
    } else {        
        console.log(res);
    }
}

function showLoggedIn() {
    $('.croc-landing-viewable').hide();
    $('.croc-login-viewable').hide();
    $('.croc-login-clickable').hide();
    $('.croc-info').hide();
    $('.croc-loggedin-viewable').show();
    $('.croc-logout-clickable').show();
    $('#croc-loggedin-username').show();    
    $('#croc-loggedin-info').show();    
}

function clickLogin(event) {
    gapi.auth.authorize({
        client_id: clientId, 
        scope: scopes, 
        immediate: false
    }, processAuthResult);
    return false;
}

function showReadyAuth() {
    $('.croc-login-clickable').show();
    $('.croc-login-viewable').show();    
    $('.croc-login-clickable').click(clickLogin);
}

function getPlus() {
    gapi.client.load('plus', 'v1', function() {
        gapi.client.plus.people.get({
            'userId': 'me'
        }).execute(setMe);
    });
}

function setPlus(me) {
    $('#croc-login').hide();
    $('#croc-username-text').text(me.displayName);
    $('#croc-user-picture').attr('src', me.image.url);            
    $('#croc-username').show();
}
    
function showLanding() {
    showLoggedOut();
}

function showLoggedOut() {
    $(".croc-info").hide();
    $("#croc-info-landing").show();
    $('.croc-landing-viewable').show();
    $('.croc-login-clickable').show();
    $('.croc-login-viewable').show();
    $('.croc-loggedin-viewable').hide();
    $('.croc-logout-clickable').hide();    
}

function clickLogout(event) {
    $.post(
        '/logout',
        null,
        processLogout
        );                
}

function processLogout(res) {
    console.log('logout response received');
    if (res.email != null) {
        $('#croc-username-text').text(null);
        $('#croc-user-picture').attr('src', null);
        showLoggedOut();
    }
}

function clickGenKey() {
    //$('#croc-genkey-modal').modal('show');
    $.post(
        '/genKey',
        null,
        processGenKey
        );              
}

function clickSignCert() {
    $.post(
        '/signCert',
        null,
        processSignCert
        );    
}

function clickResetOtp() {
    $('#croc-resetotp-modal').modal('show');
    if (false) {
        $.post(
            '/resetOtp',
            null,
            processResetOtp
            );      
    }
}

function okResetOtp() {
    console.log('okResetOtp');
    $('#croc-resetotp-modal').modal('hide');
    
}

function cancelResetOtp() {
    console.log('cancelResetOtp');
    $('#croc-resetotp-modal').modal('hide');
    
}

function okGenKey() {
    console.log('okGenKey');
    $('#croc-genkey-modal').modal('hide');
}

function cancelGenKey() {
    console.log('cancelGenKey');
    $('#croc-genkey-modal').modal('hide');    
}

function processGenKey() {
    console.log('processGenKey');
}

function processResetOtp() {
    console.log('resetOtp');
}

function submitGenKey(event) {
    event.preventDefault();
    var password = $("croc-genkey-password-input").val;
    $.post(
        '/genKey',
        password,
        processGenKeyForm
        );                  
}

function processGenKeyForm(res) {
    console.log('processGenKeyForm');
    console.log(res);
}

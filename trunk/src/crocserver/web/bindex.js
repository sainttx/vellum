
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
    $('.croc-edit-org-clickable').click(clickEditOrg);
    $('.croc-edit-network-clickable').click(clickEditNetwork);
    $('.croc-edit-host-clickable').click(clickEditHost);
    $('.croc-edit-client-clickable').click(clickEditClient);
    $('.croc-edit-service-clickable').click(clickEditService);
    $('.croc-home-clickable').click(clickHome);
    $('.croc-about-clickable').click(clickAbout);
    $('.croc-contact-clickable').click(clickContact);
    $('.croc-logout-clickable').click(clickLogout);
    $('.croc-login-clickable').click(clickLogin);
    $('#croc-edit-org').load('edit-org.html', function() {
        $('#croc-editOrg-form').submit(submitEditOrg);        
    });
    $('#croc-edit-network').load('edit-network.html', function() {
        $('#croc-editNetwork-form').submit(submitEditNetwork);        
    });
    $('#croc-edit-host').load('edit-host.html', function() {
        $('#croc-editHost-form').submit(submitEditHost);        
    });
    $('#croc-edit-client').load('edit-client.html', function() {
        $('#croc-editClient-form').submit(submitEditClient);
    });
    $('#croc-edit-service').load('edit-service.html', function() {
        $('#croc-editService-form').submit(submitEditService);
    });
    $('#croc-genkey-form').submit(submitGenKey);        
    $('.croc-account-genKey-clickable').click(clickGenKey);
    $('.croc-account-signCert-clickable').click(clickSignCert);
    $('.croc-account-resetOtp-clickable').click(clickResetOtp);
    $('#croc-secureUrl-anchor').attr('href', secureUrl);
    $('#croc-secureUrl-anchor').text(secureUrl);
    notify("Welcome");
    $(document).ajaxError(
        function (event, jqXHR, ajaxSettings, thrownError) {
            console.log('ajax error [event:' + event + '], [jqXHR:' + jqXHR + '], [ajaxSettings:' + ajaxSettings + '], [thrownError:' + thrownError + '])');
            console.log(event);
            console.log(jqXHR);
            console.log(ajaxSettings);
            console.log(thrownError);
        }
        );    
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
        $('#croc-totp-url').attr('href', res.qr);
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
    console.log('submitGenKey');    
    if (false) {
        event.preventDefault();
        $.post(
            '/genKey',
            $("#croc-genkey-form").serialize(),
            processGenKeyForm
            );
    }
    if (false) {
        $.ajax({ 
            type: 'POST',                
            url: '/genkey',
            data: {
                password: password
            },
            success: processGenKeyForm,
            error: function(xhr, status, err) {
                console.log("error");
            }
        });
    }    
    return true;
}

function processGenKeyForm(res) {
    console.log('processGenKeyForm');
    console.log(res);
}

function clickEditOrg() {
    console.log('clickEditOrg');
    $('.croc-info').hide();
    $('#croc-edit-org').show();
}

function clickEditNetwork() {
    console.log('clickEditNetwork');
    $('.croc-info').hide();
    $('#croc-edit-network').show();
}

function clickEditHost() {
    console.log('clickEditHost');
    $('.croc-info').hide();
    $('#croc-edit-host').show();
}

function clickEditClient() {
    console.log('clickEditClient');
    $('.croc-info').hide();
    $('#croc-edit-client').show();
}

function clickEditService() {
    console.log('clickEditService');
    $('.croc-info').hide();
    $('#croc-edit-service').show();
}

function submitEditOrg(event) {
    console.log('submitEditOrg');    
    event.preventDefault();
    $.post(
        '/editOrg',
        $('#croc-editOrg-form').serialize(),
        processEditOrg
        ).error(errorEditOrg);
    return false;
}

function processEditOrg(res) {
    console.log('processEditOrg');    
}

function errorEditOrg() {
    console.log('errorEditOrg');    
}

function submitEditNetwork(event) {
    console.log('submitEditNetwork');    
    event.preventDefault();
    $.post(
        '/editNetwork',
        $('#croc-editNetwork-form').serialize(),
        processEditNetwork
        ).error(errorEditNetwork);
    return false;
}

function submitEditHost(event) {
    console.log('submitEditHost');    
    event.preventDefault();
    $.post(
        '/editHost',
        $('#croc-editHost-form').serialize(),
        processEditHost
        ).error(errorEditHost);
    return false;
}

function submitEditClient(event) {
    console.log('submitEditClient');    
    event.preventDefault();
    $.post(
        '/editClient',
        $('#croc-editClient-form').serialize(),
        processEditClient
        ).error(errorEditClient);
    return false;
}

function submitEditService(event) {
    console.log('submitEditService');    
    event.preventDefault();
    $.post(
        '/editService',
        $('#croc-editService-form').serialize(),
        processEditService
        ).error(errorEditService);
    return false;
}


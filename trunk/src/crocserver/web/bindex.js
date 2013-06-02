function initTest() {
    console.log('initTest');
    initTestMeta();
}

var serverTest = {
    accessToken: '',
    checkAuth: function() {
        var res = {
            access_token: 'dummy_access_token'
        };
        processAuthResult(res);        
    },
    ajax: function(req) {
        console.log('server.ajax: ' + req.url);
        res = mockRes(req);
        console.log(res);
        req.success(res);
    },
    loginClick: function(event) {
        processAuthResult(res);
    },
    startGoogleClient: function() {        
    },
    getPlus: function() {        
    },
    initServer: function() {        
    },
    initData: function() {
    },
    initTest: function() {
    }
};

var serverReal = {
    accessToken: '',
    ajax: function(req) {
        $.ajax(req);
    },
    checkAuth: function() {
        gapi.auth.authorize({
            client_id: clientId, 
            scope: scopes, 
            immediate: true
        }, processAuthResult);
    },
    loginClick: function(event) {
        gapi.auth.authorize({
            client_id: clientId, 
            scope: scopes, 
            immediate: false
        }, processAuthResult);
        return false;
    },
    startGoogleClient: function() {        
        gapi.client.setApiKey(apiKey);
        window.setTimeout(checkAuth, 1);
    },
    getPlus: function() {
        gapi.client.load('plus', 'v1', function() {
            gapi.client.plus.people.get({
                'userId': 'me'
            }).execute(setMe);
        });
    },
    initServer: function() {        
    },
    initData: function() {
        initData();
    },
    initTest: function() {
    }
};

var server = serverReal; 

function initServer() {
    if (window.location.protocol == "file:") {
        server = serverTest; 
    }
    server.initServer();
    server.initData();
    server.checkAuth();
    server.initTest();
}

function aboutClick() {
    console.log("aboutClick");
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-about").show();        
}

function homeClick() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-landing").show();
}        

function reloadClick() {
    window.location.reload();
}        

function contactClick() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-contact").show();
}

function documentReadyRedirect() {
    console.log("documentReadyRedirect");
    if (!redirectDocument()) {
        documentReady();
    }
}
function redirectDocument() {
    console.log("redirectDocument " + window.location.protocol);
    if (window.location.protocol == "http:") {
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

function initLib() {
    if (!String.prototype.format) {
        String.prototype.format = function() {
            var args = arguments;
            return this.replace(/{(\d+)}/g, function(match, number) {
                return args[number];
            });
        };
    }
}

function documentReady() {
    initLib();
    console.log("documentReady");
    orgListLoad();
    editOrgLoad();
    $('.editNetworkClick').click(editNetworkClick);
    $('.editHostClick').click(editHostClick);
    $('.editClientClick').click(editClientClick);
    $('.editServiceClick').click(editServiceClick);
    $('.homeClick').click(homeClick);
    $('.reloadClick').click(reloadClick);
    $('.aboutClick').click(aboutClick);
    $('.contactClick').click(contactClick);
    $('.logoutClick').click(logoutClick);
    $('.loginClick').click(server.loginClick);
    $('#editNetwork').load('editNetwork.html', function() {
        $('#editNetwork-form').submit(editNetworkSubmit);        
    });
    $('#editHost').load('editHost.html', function() {
        $('#editHost-form').submit(editHostSubmit);        
    });
    $('#editClient').load('editClient.html', function() {
        $('#editClient-form').submit(editClientSubmit);
    });
    $('#editService').load('editService.html', function() {
        $('#editService-form').submit(editServiceSubmit);
        initTest();
    });
    $('#croc-genkey-form').submit(genKeySubmit);        
    $('.genKeyClick').click(genKeyClick);
    $('.signCertClick').click(signCertClick);
    $('.resetOtpClick').click(resetOtpClick);
    $('#croc-secureUrl-anchor').attr('href', secureUrl);
    $('#croc-secureUrl-anchor').text(secureUrl);
    notify("Welcome");
    initServer();
}

function notify(message) {
    console.log("notify " + message);
}

function processAuthResult(res) {
    console.log(res);
    if (res && !res.error) {
        showBusyAuth();
        processAccessToken(res.access_token);
    } else {
        showReadyAuth();
        console.log("login required");
    }
}

function showBusyAuth() {
    $('.loginClick').hide();
    $('.croc-login-viewable').hide();
    $('.logoutClick').hide();
    $('.croc-loggedin-viewable').hide();          
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
    $('.loginClick').hide();
    $('.croc-info').hide();
    $('.croc-loggedin-viewable').show();
    $('.logoutClick').show();
    $('#croc-loggedin-username').show();    
    $('#croc-loggedin-info').show();    
}

function showReadyAuth() {
    $('.loginClick').show();
    $('.croc-login-viewable').show();    
    $('.loginClick').click(server.loginClick);
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
    $('.croc-loggedin-viewable').hide();
    $('.logoutClick').hide();    
    $("#croc-info-landing").show();
    $('.croc-landing-viewable').show();
    $('.loginClick').show();
    $('.croc-login-viewable').show();
}

function processLogout(res) {
    console.log('logout response received');
    if (res.email != null) {
        $('#croc-username-text').text(null);
        $('#croc-user-picture').attr('src', null);
        showLoggedOut();
    }
}

function okResetOtp() {
    console.log('okResetOtp');
    $('#resetotp-modal').modal('hide');
    
}

function cancelResetOtp() {
    console.log('cancelResetOtp');
    $('#resetotp-modal').modal('hide');
    
}

function okGenKey() {
    console.log('okGenKey');
    $('#genkey-modal').modal('hide');
}

function cancelGenKey() {
    console.log('cancelGenKey');
    $('#genkey-modal').modal('hide');    
}

function processGenKey() {
    console.log('processGenKey');
}

function processResetOtp() {
    console.log('resetOtp');
}

function processGenKeyForm(res) {
    console.log('processGenKeyForm');
    console.log(res);
}

function buildInput(formName, field) {
    console.log(field);
    var fieldName = 'url';
    var fieldId = formName + '-' + fieldName;
    return '<input type="text" name="' + fieldName + '" placeholder="' + field.label + '" id="' + fieldId + '"/>';
}

function buildInputs(fieldset, formName, fields) {
    console.log(fields);
    fieldset.empty();
    for (var i = 0; i < fields.length; i++) {
        fieldset.append(buildInput(formName, fields[i]));
    }    
}

function editNetworkClick() {
    console.log('editNetworkClick');
    $('.croc-info').hide();
    $('#editNetwork').show();
}

function editHostClick() {
    console.log('editHostClick');
    $('.croc-info').hide();
    $('#editHost').show();
}

function editClientClick() {
    console.log('editClientClick');
    $('.croc-info').hide();
    $('#editClient').show();
}

function editServiceClick() {
    console.log('editServiceClick');
    $('.croc-info').hide();
    $('#editService').show();
}

function buildTr(handler, index) {
    var object = handler.list[index];
    var array = handler.columnArray(object);
    var html = "<tr onclick='list" + handler.name + "RowClick(" + handler.id(object) + ")'>";
    for (var i = 0; i < array.length; i++) {
        html += '<td>' + array[i] + '</td>';
    }
    html += '</tr>';
    console.log(html);
    return html;
}

function buildTable(tbody, handler) {
    tbody.empty();
    for (var i = 0; i < handler.list.length; i++) {
        tbody.append(buildTr(handler, i));
    }    
}

function processAccessToken(accessToken) {
    server.accessToken = accessToken;
    console.log(accessToken);
    server.ajax({ 
        type: 'POST',                
        url: '/login',
        data: 'accessToken=' + accessToken,
        success: processLogin,
        error: processLoginError
    });
}

function logoutClick(event) {
    server.ajax({
        type: 'POST',                
        url: '/logout',
        data: null,
        success: processLogout,
        error: processLogoutError
    });                
}

function resetOtpClick() {
    $('#croc-resetotp-modal').modal('show');
    if (false) {
        $.post(
            '/resetOtp',
            null,
            processResetOtp
            );      
    }
}

function genKeyClick() {
    //$('#croc-genkey-modal').modal('show');
    $.post(
        '/genKey',
        null,
        processGenKey
        );              
}

function signCertClick() {
    $.post(
        '/signCert',
        null,
        processSignCert
        );    
}

function genKeySubmit(event) {
    console.log('genKeySubmit');    
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

function editNetworkSubmit(event) {
    console.log('editNetworkSubmit');    
    event.preventDefault();
    $.post(
        '/editNetwork',
        $('#croc-editNetwork-form').serialize(),
        processEditNetwork
        ).error(errorEditNetwork);
    return false;
}

function editHostSubmit(event) {
    console.log('editHostSubmit');    
    event.preventDefault();
    $.post(
        '/editHost',
        $('#croc-editHost-form').serialize(),
        processEditHost
        ).error(errorEditHost);
    return false;
}

function editClientSubmit(event) {
    console.log('editClientSubmit');    
    event.preventDefault();
    $.post(
        '/editClient',
        $('#croc-editClient-form').serialize(),
        processEditClient
        ).error(errorEditClient);
    return false;
}

function editServiceSubmit(event) {
    console.log('editServiceSubmit');    
    event.preventDefault();
    $.post(
        '/editService',
        $('#croc-editService-form').serialize(),
        processEditService
        ).error(errorEditService);
    return false;
}


function initTest() {
    console.log('initTest');
}

var mockServer = {
    auth: '',
    googleAccessToken: '',
    googleLoginAuthorize: function() {
        var res = {
            access_token: 'dummy_access_token'
        };
        googleLoginAuthorizeRes(res);        
    },
    ajax: function(req) {
        console.log('mockServer.ajax', req.url, req.data);
        res = mockRes(req);
        console.log("mockServer res", res);
        req.success(res);
    },
    googleClient: function() {        
    },
    getPlus: function() {        
    }
};

var contact1 = {
    name: 'Joe Soap',
    mobile: '27827779988',
    email: 'joe@gmail.com',
}

var contact2 = {
    name: 'Ginger Bread',
    mobile: '2783667300',
    email: 'gingerb@gmail.com',
}

var testLogin = {
    name: 'Testy Tester',
    email: 'test@gmail.com',
    picture: '',
    totpSecret: '',
    totpUrl: '',
    qr: '',
    contacts: ['Ginger', 'Harry', 'Ian', 'Jenny']
}

var testLogout = {
    email: 'test@gmail.com'
}

var bizOrg = {
    orgId: 1,
    orgUrl: 'biz.net',
    orgCode: 'biz',
    displayName: 'Biz (Pty) Ltd',
    region: 'Western Cape',
    locality: 'Cape Town',
    country: 'South Africa'
}

var otherOrg = {
    orgId: 2,
    orgUrl: 'other.net',
    orgCode: 'other',
    displayName: 'The Other Company (Pty) Ltd',
    region: 'Western Cape',
    locality: 'Cape Town',
    country: 'South Africa'    
}

var mock = {
    orgList: [bizOrg, otherOrg],
    contactList: [contact1, contact2]
}

function mockRes(req) {
    if (req.url === '/googleLogin') {
        return testLogin;
    } else if (req.url === '/personaLogin') {
        return testLogin;
    } else if (req.url === '/logout') {
        return testLogout;
    } else if (req.url === '/contactEdit') {
        return req.data;
    } else if (req.url === '/contactAdd') {
        console.log('mockRes memo', req.memo);
        mock.contactList.push(req.memo);
        testLogin.contacts.push(req.memo.name);
        return req.data;
    } else if (req.url === '/contactList') {
        return mock.contactList;
    }
    return {
        error: 'mockRes ' + req.url
    }
}


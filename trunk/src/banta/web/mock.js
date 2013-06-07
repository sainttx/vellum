
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

var mockData = {
    login: {
        name: 'Testy Tester',
        email: 'test@gmail.com',
        picture: '',
        totpSecret: '',
        totpUrl: '',
        qr: '',
    },
    logout: {
        email: 'test@gmail.com'
    },
    contacts: [
        {
            name: 'Joe Soap',
            mobile: '27827779988',
            email: 'joe@gmail.com',
        },
        {
            name: 'Ginger Bread',
            mobile: '2783667300',
            email: 'gingerb@gmail.com',
        },
        {
            name: 'Harry Potter',
            mobile: '2783667400',
            email: 'harryp@gmail.com',
        }
    ],
    orgs: [
        {
            orgId: 1,
            orgUrl: 'biz.net',
            orgCode: 'biz',
            displayName: 'Biz (Pty) Ltd',
            region: 'Western Cape',
            locality: 'Cape Town',
            country: 'South Africa'
        },
        {
            orgId: 2,
            orgUrl: 'other.net',
            orgCode: 'other',
            displayName: 'The Other Company (Pty) Ltd',
            region: 'Western Cape',
            locality: 'Cape Town',
            country: 'South Africa'
        }
    ]
};


function mockRes(req) {
    if (req.url === '/googleLogin') {
        mockData.login.contacts = mockData.contacts;
        return mockData.login;
    } else if (req.url === '/personaLogin') {
        mockData.login.contacts = mockData.contacts;
        return mockData.login;
    } else if (req.url === '/logout') {
        return mockData.logout;
    } else if (req.url === '/contactEdit') {
        return req.data;
    } else if (req.url === '/contactEdit') {
        console.log('mockRes memo', req.memo);
        mockData.contacts.push(req.memo);
        return req.data;
    } else if (req.url === '/contactList') {
        return mock.contacts;
    }
    return {
        error: 'mockRes ' + req.url
    }
}


var global = {
    mobileClient: false,
    savePermit: true,
    usd: 0,
    eur: 0
};

/**
 * Oauth2
 */

function requestOauthToken(username, password) {

	var success = false;
	var basicHeader = "Basic " + btoa(username + ":" + password);
	$.ajax({
		url: 'uaa/user',		
		type: 'get',
		beforeSend: function (xhr) {
		    xhr.setRequestHeader ("Authorization", basicHeader);
		},
		data: {
			submit: 'Login',
			username: username,
			username: password
		},
		success: function (data) {
			localStorage.setItem('username', data.name);
			localStorage.setItem('token', basicHeader);
			success = true;		

	        initAccount(getCurrentAccount());

	        var userAvatar = $("<img />").attr("src","images/userpic.jpg");
	        $(userAvatar).load(function() {
	            setTimeout(initGreetingPage, 500);
	        });		    
		},
		error: function (xhr, status, err) {
			alert('Exeption:'+err);
			removeOauthTokenFromStorage();
			$("#preloader, #enter, #secondenter").hide();
	        flipForm();
	        $('.frontforms').val('');
	        $("#frontloginform").focus();
	        alert("Something went wrong. Please, check your credentials");
		}
	});

	return success;
}

function getOauthTokenFromStorage() {
	return localStorage.getItem('token');
}

function removeOauthTokenFromStorage() {
    return localStorage.removeItem('token');
}

/**
 * Current account
 */

function getCurrentAccount() {

	var token = getOauthTokenFromStorage();
	var account = null;

	if (token) {
		$.ajax({
			url: 'uaa/current',
			datatype: 'json',
			type: 'get',
			headers: {'Authorization': token},
			async: false,
			success: function (data) {
				account = data;
			},
			error: function () {
				removeOauthTokenFromStorage();
			}
		});
	}

	return account;
}

$(window).load(function(){

	if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		FastClick.attach(document.body);
        global.mobileClient = true;
	}

    $.getJSON("http://api.fixer.io/latest?base=RUB", function( data ) {
        global.eur = 1 / data.rates.EUR;
        global.usd = 1 / data.rates.USD;
    });

	var account = getCurrentAccount();

	if (account) {
		showGreetingPage(account);
	} else {
		showLoginForm();
	}
});

function showGreetingPage(account) {
    initAccount(account);
	var userAvatar = $("<img />").attr("src","images/userpic.jpg");
	$(userAvatar).load(function() {
		setTimeout(initGreetingPage, 500);
	});
}

function showLoginForm() {
	$("#loginpage").show();
	$("#frontloginform").focus();
	setTimeout(initialShaking, 700);
}
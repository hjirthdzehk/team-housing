/** Created by a.kiselev on 22/11/2016. */

var UserService = function () {
    var setCookie = function (cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays*24*60*60*1000));
        var expires = "expires="+d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    };

    var getCookie = function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    };

    this.login = function (login, pass) {
        return $.post('/dwellers/signIn', {
            'email': login,
            'passwordHash': md5(pass)
        }).then(function(personId) {
            return $.get('/dwellers/isAdmin/' + personId).then(function(isAdmin) {
                setCookie("personId", personId);
                setCookie("isAdmin", isAdmin);
                return $.Deferred().resolve(personId).promise();
            });
        });
    };

    this.getPersonId = function () {
        return getCookie("personId");
    };

    this.isLogined = function() {
        return !!getCookie("personId");
    };

    this.logOut = function() {
        setCookie("personId", '',0);
        setCookie("isAdmin", '',0);
    };


    this.isAdmin = function () {
        return getCookie("isAdmin");
    }
};
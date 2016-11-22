/** Created by a.kiselev on 22/11/2016. */

var UserService = function () {
    this.login = function (login, pass) {
        return $.post('/dwellers/signIn', {
            'email': login,
            'passwordHash': md5(pass)
        }).done(function (personId) {
            Cookies.set('person_id', personId, {path: "/", expires: 7});
        });
    };

    this.getPersonId = function () {
        return Cookies.get('person_id');
    }
};
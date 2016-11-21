/** Created by a.kiselev on 16/11/2016. */

var SignInViewModel = function () {
    var self = this;

    self.email = ko.observable('');
    self.password = ko.observable('');

    self.signIn = function () {
        new UserService().login(self.email(), self.password())
            .then(function (personId) {
                window.location = '#/dwellers/show/' + personId;
            });
    }
};
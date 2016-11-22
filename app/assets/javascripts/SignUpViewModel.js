/** Created by a.kiselev on 16/11/2016. */

var SignUpViewModel = function() {
    var self = this;
    var userService = new UserService();

    self.name = ko.observable('');
    self.surname = ko.observable('');
    self.paternalName = ko.observable('');
    self.email = ko.observable('');
    self.password = ko.observable('');

    self.register = function() {
        $.post('/dwellers/signUp', {
            'name' : self.name(),
            'surname' : self.surname(),
            'paternalName': self.paternalName(),
            'email': self.email(),
            'passwordHash': md5(self.password())
        }).then(function () {
            userService.login(self.email(), self.password()).then(function() {
                window.location = '/';
            });
        });
    }
};
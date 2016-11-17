/** Created by a.kiselev on 16/11/2016. */

var SignUpModel = function() {
    var self = this;

    self.name = ko.observable('');
    self.surname = ko.observable('');
    self.paternalName = ko.observable('');

    self.register = function() {
        $.post('/dwellers/signUp', {
            'name' : self.name(),
            'surname' : self.surname(),
            'paternalName' : self.paternalName()
        }).then(function() {
            self.name('');
        });
    }
};
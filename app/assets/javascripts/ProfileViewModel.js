/** Created by a.kiselev on 22/11/2016. */

var ProfileViewModel = function (profileData, personId) {
    var self = this;
    self.personId = personId;
    self.name = ko.observable(profileData.name);
    self.surname = ko.observable(profileData.surname);
    self.paternalName = ko.observable(profileData.paternalName);
    self.email = ko.observable(profileData.email);
    self.flats = ko.observableArray(profileData.flats);

    self.fullName = self.surname() + ' ' + self.name() + ' ' + self.paternalName();
};
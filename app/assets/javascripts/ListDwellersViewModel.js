/** Created by a.kiselev on 16/11/2016. */

var ListDwellersViewModel = function (persons) {
    self.fields = Object.keys(persons).map(function(field) {
        return { 'field' : field }
    });
    self.persons = ko.observableArray(persons);
};
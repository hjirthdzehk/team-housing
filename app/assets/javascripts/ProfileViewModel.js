/** Created by a.kiselev on 31/10/2016. */

var ProfileViewModel = function(profileData) {
    var self = this;

    self.personName = ko.observable(profileData.personName);
    self.flats = ko.observableArray(_.map(profileData.metersByFlat,
        function (meters, flat) {
            return {
                flatNo: ko.observable(flat),
                meters: ko.observableArray(_.map(meters, function(meter) {
                    return {
                        title: meter.title,
                        value: -1, // TODO change to actual
                        unit: meter.unit
                }
            }))
        }
    }));

};
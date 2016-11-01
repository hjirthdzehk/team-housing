/** Created by a.kiselev on 31/10/2016. */

var MetersViewModel = function(profileData, canInputReadings) {
    var self = this;
    self.canInputReadings = canInputReadings;
    self.personName = ko.observable(profileData.personName);
    self.flats = ko.observableArray(_.map(profileData.metersByFlat,
        function (meters, flat) {
            return {
                flatNo: ko.observable(flat),
                meters: ko.observableArray(_.map(meters, function(meter) {
                    return {
                        id: meter.id,
                        title: meter.title,
                        unit: meter.unit,
                        value: ko.observable(0)
                }
            }))
        }
    }));


    if (canInputReadings) {
        self.date = ko.observable('');
        self.submit = function () {
            var readings = _.chain(self.flats())
                .map(function(flat) {
                    return flat.meters();
                })
                .flatten()
                .map(function(meterReading) {
                    return {
                        meterId: meterReading.id,
                        value: meterReading.value(),
                        date: self.date()
                    };
                }).value();
            readings.forEach(function(reading) {
                $.post('/meterReadings', reading)
            });
        };
    }
};
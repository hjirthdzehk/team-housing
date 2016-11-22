/** Created by a.kiselev on 31/10/2016. */

var MetersViewModel = function(profileData, canInputReadings) {
    var self = this;
    self.canInputReadings = canInputReadings;
    self.flats = ko.observableArray(_.map(profileData.flats,
        function (flat) {
            return {
                flatId: flat.flatId,
                flatNumber: ko.observable('#' + flat.flatNumber),
                meters: ko.observableArray(_.map(flat.meters, function (meter) {
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
        self.submit = function () {
            var readings = _.chain(self.flats())
                .map(function (flat) {
                    return flat.meters();
                })
                .flatten()
                .filter(function (reading) {
                    return reading.value() !== 0;
                })
                .map(function(meterReading) {
                    return {
                        meterId: meterReading.id,
                        value: meterReading.value
                    };
                });
            readings.forEach(function(reading) {
                $.post('/meterReadings', reading)
            });
        };
    }
};
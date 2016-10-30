var MeasuresInputViewModel = function(groups) {
    var self = this;
    self.date = ko.observable('');
    self.meterGroups = ko.observableArray(_.map(groups, function (group) {
        group.meters = _.map(group.meters, function (meter) {
            meter.value = ko.observable(0);
            return meter;
        });
        return group;
    }));
    self.submit = function () {
         var readings = _.chain(self.meterGroups())
            .map(function(group) {
                return group.meters;
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
};
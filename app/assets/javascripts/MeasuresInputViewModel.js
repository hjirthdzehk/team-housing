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
        console.log(ko.toJS(this));
    };
};
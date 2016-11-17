var MetersStatisticsViewModel = function() {

    self.meters = ko.observableArray([]);
    $.get('/meters/1').then(function(meters) {
        self.meters(meters.meterListItems);
    });
    self.from = ko.observable('');
    self.to = ko.observable('');
    self.selectedMeter = ko.observable(self.meters()[1]);
    self.metersCostData = ko.observableArray([]);
    self.refresh = function() {
        $.get('/getReadingsCosts/' + self.selectedMeter().meterId, {
            dateFrom: self.from(),
            dateTo: self.to()
        }).then(function(data) {
            self.metersCostData(data.readingCosts.map(function(readingCost){
                readingCost.date = moment(readingCost.date).format('MMM Do YYYY');
                return readingCost;
            }));
        });
    }
};

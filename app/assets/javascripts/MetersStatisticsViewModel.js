var MetersStatisticsViewModel = function(profileData, personId) {
    self.meters = ko.observableArray([]);
    $.get('/meters/'+personId).then(function(meters) {
        self.meters(meters.meterListItems);
    });

    var currentDate = new Date().toJSON().slice(0, 10);
    var beforeDate = new Date(1319673600000).toJSON().slice(0, 10)

    self.from = ko.observable(beforeDate);
    self.to = ko.observable(currentDate);
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

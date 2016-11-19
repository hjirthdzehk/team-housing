
var MetersCreateViewModel = function() {
    var self = this;

    self.title = ko.observable('');
    self.type = ko.observable('');
    self.meterUnitId = ko.observable('');
    self.flatId = ko.observable('');

    self.createMeter = function() {
    console.log("hello")
        $.post('/api/meters', {
            'title'         : self.title(),
            'type'          : self.type(),
            'meterUnitId'   : self.meterUnitId(),
            'flatId'        : self.flatId()
        }).then(function () {
            self.title('');
            self.type('');
            self.meterUnitId('');
            self.flatId('');
        });
    }
};
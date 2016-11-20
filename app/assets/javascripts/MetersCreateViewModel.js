
var MetersCreateViewModel = function() {
    var self = this;

    this.title = ko.observable('');
    this.flatId = ko.observable('');

    var waterMeterType          = {id: 1, name: 'Water'};
    var electricityMeterType    = {id: 2, name: 'Electricity'};

    this.availableMeterTypes = ko.observableArray([
      waterMeterType,
      electricityMeterType
    ]);
    this.selectedMeterType = ko.observable(waterMeterType);

    self.createMeter = function() {
        try {
            if (self.title() !== '') {
                $.post('/api/meters', {
                    'title'         : self.title(),
                    'type'          : this.selectedMeterType().name,
                    'meterUnitId'   : this.selectedMeterType().id, // meterUnitId corresponds to id in meterType
                    'flatId'        : self.flatId()
                }).then(function () {
                    self.title('');
                    self.flatId('');
                });
            } else {
                self.title('Title not set')
            }
        } catch (e) {
            // selectedMeterType is not set
        }
    }
};
/**
 * Created by VladVin on 02.11.2016.
 */

var MeasurementsHistoryViewModel = function (meterHistory) {
    var self = this;

    self.meterHistoryGroup = ko.observableArray(
        _.map(meterHistory,
            function (meter) {
                return {
                    title: ko.observable(meter.meterTitle),
                    meterReadings: ko.observableArray(_.map(
                        meter.meterReadings,
                        function (meterReading) {
                            return {
                                value: ko.observable(meterReading.value),
                                unit: ko.observable(meter.meterUnitTitle),
                                date: ko.observable(moment(meterReading.date).format('MMM-DD-YYYY HH:MM')),
                                paid: ko.observable(meterReading.paid ? "Paid" : "Not paid")
                            }
                        }
                    )),
                    plotData: {
                        labels: _.map(meter.meterReadings,
                            function (meterReading) {
                                return moment(meterReading.date).format('MMM-DD-YYYY HH:MM')
                            }),
                        datasets: [{
                            label: meter.meterTitle,
                            data: _.map(meter.meterReadings, function (meterReading) {
                                return meterReading.value
                            }),
                            backgroundColor: "rgba(220,220,220,0.2)",
                            borderColor: "rgba(220,220,220,1)",
                            pointColor: "rgba(220,220,220,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(220,220,220,1)"
                        }]
                    }
                }
            }
        ));
};
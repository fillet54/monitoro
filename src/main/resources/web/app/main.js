var context = cubism.context()
              .step(1e3) // Distance between data points in milliseconds
              .size(960); // Number of data points
              //.stop();

var metricNames = [];

function collection(name) {
    return context.metric(function(start, stop, step, callback) {
    	
    	var points = (stop.getTime() - start.getTime()) / step;
        d3.json("/blueflood/v2.0/master/views/" + name + "?from=" + start.getTime()
              + "&to=" + stop.getTime()
              + "&points=" + points, function(data) {
                if(!data) return callback(new Error("unable to load data"));
                callback(null, rollupCollectionData(data, start.getTime(), step));
        });
	}, name);
}

// The blueflood service will not fully roll up all data. Instead
// it rolls up data at very distinct resolutions. If data is requested
// at a resolution that is in between two of those distinct resolutions
// then data is function will only have to do one more roll up to get 
// the data at the resolution we want.
function rollupCollectionData(data, startInMillis, stepInMillis) {
	var rolledUpData = [];
	var slotEndInMillis = startInMillis + stepInMillis;
	var total = 0;
	var count = 0;
	
	data.values.forEach(function (d) {
		while(d.timestamp > slotEndInMillis) {
			var average = 0 // Push zero when no data points are in slot.
			if (count > 0) {
				average = total / count;
			}
			total = 0;
			count = 0;
			
			rolledUpData.push(average);
			slotEndInMillis += stepInMillis;
		}
		
		total += d.average * d.numPoints;
		count += d.numPoints;
	});
	
	return rolledUpData;
}

function draw_graph(collection_list) {
	d3.select("#demo").call(function(div) {

          metricNames = _.union(metricNames, collection_list);
          var metrics = metricNames.map(collection);

		  div.append("div")
		      .attr("class", "axis")
		      .call(context.axis().orient("top"));

		  div.append("div")
		      .attr("class", "horizonContainer")
	  	      .selectAll(".horizon")
		       .data(metrics)
		       .enter().append("div")
		        .attr("class", "horizon")
		        .call(context.horizon()
		     		         .extent([0, 1])
		    		         .format(d3.format(".2p")));

		  div.append("div")
		      .attr("class", "rule")
		      .call(context.rule());
	});
}

function addToGraph(entries) {
    metricNames = _.union(metricNames, entries);
    var metrics = metricNames.map(collection);

	d3.select("#demo").call(function(div) {
	      div.select(".horizonContainer")
		  .selectAll(".horizon")
		  .data(metrics)
		   .enter().append("div")
		    .attr("class", "horizon")
		    .call(uniquelyMark)
		    .call(context.horizon()
		                 .extent([0, 1])
		    	         .format(d3.format(".2p")));
    });
}

function uniquelyMark(selection) {
    selection.each(function(d, i) {
	    d3.select(this).attr("data-metricid", d.toString());
	});
}

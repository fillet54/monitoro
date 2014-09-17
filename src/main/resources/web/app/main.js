var context = cubism.context()
              .step(5000) // Distance between data points in milliseconds
              .size(1280); // Number of data points
              //.stop();

function stock(name) {
    return context.metric(function(start, stop, step, callback) {
    	
    	var points = (stop.getTime() - start.getTime()) / step;
        d3.json("/blueflood/v2.0/master/views/" + name + "?from=" + start.getTime()
              + "&to=" + stop.getTime()
              + "&points=" + points, function(data) {
                if(!data) return callback(new Error("unable to load data"));
                callback(null, RollUpData(data, start.getTime(), step));
        });
	}, name);
}

function RollUpData(data, startInMillis, stepInMillis) {
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

function draw_graph(stocks_list) {
	d3.select("body").append("div") // Add a vertical rule to the graph
          .attr("class", "rule")
          .call(context.rule());

    d3.select("#demo")                 // Select the div on which we want to act
      .selectAll(".axis")              // This is a standard D3 mechanism to bind data
      .data(["top"])                   // to a graph. In this case we're binding the axes
      .enter()                         // "top" and "bottom". Create two divs and give them
      .append("div")                   // the classes top axis and bottom axis respectively.
      .attr("class", function(d) {
      	return d + " axis";
      })
      .each(function(d) {              // For each of these axes, draw the axes with 4
      	d3.select(this)              // intervals and place them in their proper places.
      	  .call(context.axis()       // 4 ticks gives us an hourly axis.
      	  .ticks(4).orient(d));
      });

    d3.select("#demo")
      .selectAll(".horizon")
      .data(stocks_list.map(stock))
      .enter()
      .insert("div", ".bottom")        // Insert the graph in a div. Turn the div into
      .attr("class", "horizon")        // a horizon graph and format to 2 decimals places.
      .call(context.horizon()
    		       .scale(d3.scale.linear(1.0))
    		       .format(d3.format("+,.2p")));

    context.on("focus", function(i) {
        d3.selectAll(".value").style("right",   // Make the rule coincide with the mouse
        	i == null ? null : context.size() - i + "px");
    });
}
# Install packages
install.packages("fpp2")
install.packages("ggplot2")
install.packages("forecast")
install.packages("ggpubr")
install.packages("zoo")
install.packages("smooth")
install.packages("MLmetrics")
install.packages("scales")

## Import libraries
library(fpp2)
library(ggplot2)
library(forecast)
library(ggpubr)
library(zoo)
library(smooth)
library(MLmetrics)
library(scales)

# Time series of quarterly data
y <- ts(rnorm(n=60,mean=10,sd=1), start=2000, frequency=4)
y

autoplot(y,color="black")+
  ggtitle("Example Time Series Plot Quarterly Data") + 
  xlab("Year") + 
  ylab("Data")

# Time series of monthly data
y <- ts(rnorm(n=180,mean=10,sd=1), start=2000, frequency=12)
y

autoplot(y,color="black")+
  ggtitle("Example Time Series Plot Monthly Data") + 
  xlab("Year") + 
  ylab("Data")

# Stacked time series
x <- ts(rnorm(n=20,mean=0,sd=1), start=2000, frequency=4)
y <- ts(rnorm(n=20,mean=0,sd=1), start=2000, frequency=4)

plot_x <- autoplot(x,color="blue")+
  ggtitle("Example Time Series Plot with X-Data") + 
  xlab("Year") + 
  ylab("Data")

plot_y <- autoplot(y,color="red")+
  ggtitle("Example Time Series Plot with Y-Data") + 
  xlab("Year") + 
  ylab("Data")

example_dataset <- cbind(x,y)
autoplot(example_dataset)+
  ggtitle("Example Time Series Plot X-Y Data") + 
  xlab("Year") + 
  ylab("Data") +
  scale_color_manual(values = c("black", "red")) +
  theme_bw()

# Separated panel
ggarrange(plot_x, plot_y, ncol=2, nrow=1)

# Using ggplot
# WIP



## Modelling with naive method
# Set training data from 1992 to 2007
beer2 <- window(ausbeer,start=1992,end=c(2007,4))
# Plot some forecasts
autoplot(beer2) +
  autolayer(naive(beer2, h=11),
    series="Naïve", PI=FALSE) +
  autolayer(snaive(beer2, h=11),
    series="Seasonal naïve", PI=FALSE) +
  ggtitle("Forecasts for quarterly beer production") +
  xlab("Year") + ylab("Megalitres") +
  guides(colour=guide_legend(title="Forecast"))


## Modelling with moving average method
model_ma <- sma(goog200,order=10,h=40)
plot(forecast(model_ma,interval = FALSE),
     legend=FALSE,
     main="Google stock forecasting with MA-10",
     xlab="Day", 
     ylab="Closing Price (US$)")

## Modelling with time series regression model
# WIP

## Evaluation model
data.train <- window(ausbeer, start=1980, end=c(2000,4))
data.test <- window(ausbeer, start=2001, end=c(2004,4))
data.train
data.test

naive_forecast <- naive(data.train, h=16)
snaive_forecast <- snaive(data.train, h=16)
tslm_model <- tslm(data.train ~ trend+season)
tslm_forecast <- forecast(tslm_model, h=16)

prediction <- naive_forecast$mean
evaluation <- rbind(RMSE(prediction,data.test),
			MAPE(prediction,data.test))
rownames(evaluation) <- c("RMSE","MAPE")
colnames(evaluation) <- c("Value (Naive)")
evaluation

prediction <- snaive_forecast$mean
evaluation <- rbind(RMSE(prediction,data.test),
			MAPE(prediction,data.test))
rownames(evaluation) <- c("RMSE","MAPE")
colnames(evaluation) <- c("Value (SNaive)")
evaluation

prediction <- tslm_forecast$mean
evaluation <- rbind(RMSE(prediction,data.test),
			MAPE(prediction,data.test))
rownames(evaluation) <- c("RMSE","MAPE")
colnames(evaluation) <- c("Value (TSLM)")
evaluation
evaluation
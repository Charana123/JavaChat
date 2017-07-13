package com.charana.login_window.animations;

import com.charana.login_window.utilities.StreamUtilities;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SkypeLoadingAnimation extends Pane {

    double animationRadiusX = 30;
    double animationRadiusY = 30;
    double smallCircleRadius = 5;
    double largeCircleRadius = 10;
    double delay = 0.075;
    ParallelTransition animation;

    public SkypeLoadingAnimation(){
        setPrefSize(animationRadiusX*2 + smallCircleRadius*2, animationRadiusY*2 + smallCircleRadius + largeCircleRadius);
        setMaxSize(animationRadiusX*2 + smallCircleRadius*2, animationRadiusY*2 + smallCircleRadius + largeCircleRadius);

        //Create UI elements
        List<Circle> circles = IntStream.range(1,5).mapToObj(integer -> new Circle(smallCircleRadius, Color.web("#1DB0ED"))).collect(Collectors.toList());
        Circle centerCircle = new Circle(animationRadiusX + smallCircleRadius, largeCircleRadius, largeCircleRadius, Color.web("#1DB0ED"));

        //Add UI elements
        getChildren().addAll(circles);
        getChildren().add(centerCircle);

        //Create animation using UI elements
        animation = createAnimation(circles, centerCircle);
        animation.play();
    }

    private ParallelTransition createAnimation(List<Circle> circles, Circle centerCircle){
        //Create Moving Circles Animation
        List<PauseTransition> pauseTransitions = IntStream.range(1,5).mapToObj(i -> new PauseTransition(Duration.seconds(i * delay))).collect(Collectors.toList());
        Path path = createEllipsePath(animationRadiusX + smallCircleRadius, animationRadiusY + largeCircleRadius, animationRadiusX, animationRadiusY);
        BiFunction<Circle, PauseTransition, SequentialTransition> zipper = (circle, pauseTransition) -> new SequentialTransition(circle, pauseTransition, new PathTransition(Duration.seconds(1), path));
        List<SequentialTransition> sequentialTransitions = StreamUtilities.zip(circles.stream(), pauseTransitions.stream(), zipper).collect(Collectors.toList());
        ParallelTransition movingCirclesAnimation = ParallelTransitionBuilder.create()
                .children(sequentialTransitions)
                .build();

        //Create Resizing Circle Animation
        ScaleTransition scaleDown = ScaleTransitionBuilder.create()
                .duration(Duration.seconds(delay * 4))
                .toX(0)
                .toY(0)
                .build();
        PauseTransition pause = new PauseTransition(Duration.seconds(1.0 - delay * 4));
        ScaleTransition scaleUp = ScaleTransitionBuilder.create()
                .duration(Duration.seconds(delay*4))
                .toX(1)
                .toY(1)
                .build();
        SequentialTransition centerCircleAnimation = SequentialTransitionBuilder.create()
                .children(scaleDown, pause, scaleUp)
                .node(centerCircle)
                .build();

        //Create Final Skype Loading Animation
        ParallelTransition animation = ParallelTransitionBuilder.create()
                .children(movingCirclesAnimation, centerCircleAnimation)
                .cycleCount(Animation.INDEFINITE)
                .build();
        return animation;
    }

    private Path createEllipsePath(double centerX, double centerY, double radiusX, double radiusY){
        MoveTo moveTo = new MoveTo(centerX, centerY - radiusY);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX + 1);
        arcTo.setY(centerY - radiusY);
        arcTo.setRadiusX(radiusX);
        arcTo.setRadiusY(radiusY);
        arcTo.setLargeArcFlag(true);

        Path path = new Path(moveTo, arcTo, new ClosePath());
        return path;
    }

}

;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-advanced-reader.ss" "lang")((modname |Computer Science Final Project|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp") (lib "universe.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #t #t none #f ((lib "image.rkt" "teachpack" "2htdp") (lib "universe.rkt" "teachpack" "2htdp")) #f)))
;Final Project: Key-Master

;Height of the background
;Width of the background
;Background color
(define HEIGHT 1000)
(define WIDTH 1500)
(define BGCOLOR "black")
(define SCORE+ 5)
(define SCORE- 25)
(define MISTYPE-MAX 10)
(define POOR-SCORE 2000)
(define DECENT-SCORE 2500)
(define GOOD-SCORE 3000)
(define EXCELLENT-SCORE 3000)

;The Background
(define BACKGROUND (rectangle WIDTH HEIGHT "solid" BGCOLOR))

;The line underneath all the letters
(define LINE (rectangle WIDTH 1 "solid" "blue"))

;Short line under the current letter
(define SHORT-LINE (rectangle 16 1 "solid" "yellow"))


;STARTINGTIME: The amount of time to type in the first level
(define STARTING-TIME 60)

;MAX-MISTAKES: The maxiumum number of mistakes the player can make before game over
(define MAX-MISTAKES 10)


;UP2: List of the Uppercase Letters
(define UP (list "A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "Q" "R" "S" "T" "U" "V" "W" "X" "Y" "Z"))

;LP2: List of the Lowercase Letters
(define LC (list "a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"))

;WORD-POOL: List of the pool of words drawn upon for the game
(define WORD-POOL (list "hello " ;1
                        "boat " ;2
                        "battle " ;3
                        "taco " ;4
                        "joke " ;5
                        "tower " ;6
                        "bread "; 7
                        "butter " ;8
                        "peanut " ;9
                        "water " ;10
                        "food "; ;11
                        "boom " ;12
                        "lunch " ;13
                        "stinker " ;14
                        "bagel " ;15
                        "coding " ;16
                        "book " ;17
                        "sugar " ;18
                        "running " ;19
                        "blast " ;20
                        "warring " ;21
                        "back " ;22
                        "racket " ;23
                        "look " ;24
                        "behind " ;25
                        "crazy " ;26
                        "sane " ;27
                        "slade " ;28
                        "adieu " ;29
                        "zebra " ;30
                        "coddle " ;31
                        "cause " ;32
                        "bust " ;33
                        "crow " ;34
                        "dogs " ;35
                        "eggs " ;36
                        "fumble " ;37
                        "grumble " ;38
                        "harp " ;39
                        "igloo " ;40
                        "jump " ;41
                        "never " ;42
                        "lout " ;43
                        "music " ;44
                        "cross " ;45
                        "pass " ;46
                        "queer " ;47
                        "race " ;48
                        "silent " ;49
                        "meager " ;50
                        "nuggets " ;51
                        "vowel " ;52
                        "wacky " ;53
                        "sound " ;54
                        "yodel " ;55
                        "zap") ;56
  )




;nth-element: (List->Element)
 ;Finds the nth element in a list
(define nth-element
  (lambda [lst n]
    (cond
      [(= n 1) (first lst)]
      [else (nth-element (rest lst) (- n 1))]
    )
  )
  )

;random-element-generator: (List->ElementOfList)
 ;Provides a list and random number to generate a random element from nth-element
(define random-element-generator
  (lambda [lst]
    (nth-element lst (random 1 57))))


;random-list-generator: (List X Number -> List)
  ;Generates a list of given length made up of randomly pulled elements from another list
    ;n: the number of elements in the list
    ;l: the list of elements one wishes to pull from to construct the new randomized list
(define random-list-generator
  (lambda [n l]
    (cond
      [(= n 0) empty]
      [else (cons (random-element-generator l) (random-list-generator (- n 1) l))]
      )
    )
  )

;word-to-char: (String->ListOf(char))
  ;takes a string and converts it to a list of characters
(define word-to-char
  (lambda [n]
    (string->list n)))

;char-to-string: (ListOf(char))
  ;Takes a list of char from word-to-char and converts it into a list of strings of individual letters
(define char-to-string
  (lambda [l]
    (cond
      [(empty? l) empty]
      [else (cons (list->string (cons (first l) empty)) (char-to-string (rest l)))]
      )
    )
  )

;word-combiner (ListOf(String)->ListOf(String))
  ;Takes a list of words generated from random-list-generator, gets a list of strings of letters through word-to-char and char-to-string, and combines all the
  ;lists of letters into one long list of strings of letters
(define word-combiner
  (lambda [l]
    (cond
      [(empty? l) empty]
      [else (append (char-to-string (word-to-char (first l))) (word-combiner (rest l)))]
      )
    )
  )
  
;WS: (Struct) The Worldstate
  ;score: The Number of points the player has gained from typing the correct words or completing levels
  ;mistypes: The number of mistakes the player makes, with 5 causing a game-over
  ;timer-speed: The amount of time you will have for a given level, the higher the level the less time will be available
  ;lol: The list of elements that will be present on the screen for the player to type out
  ;lvl: The current level the Player is on
(define-struct WS [score mistypes timer lol lvl])


;init-WS: The initial worldstate of the game
(define init-WS
  (make-WS 0 0 40 (word-combiner (random-list-generator 14 WORD-POOL)) 1))

;timer-Changer (WS->Number)
   ;Accepts the current level number from tock and subtracts 10 times that number from your starting time for the next level
(define timer-changer
  (lambda [b]
    (- 40 (* 5 b))
    )
  )

;tock: (WS->WS)
   ;Controls how quickly the time counts down amd generates a new world-state when the list of letters is empty
(define tock
  (lambda [ws]
    (cond
      [(<= (length (WS-lol ws)) 1)
       (make-WS (WS-score ws) (WS-mistypes ws) (timer-changer (WS-lvl ws)) (word-combiner (random-list-generator 14 WORD-POOL)) (+ 1 (WS-lvl ws)))]
      [else (make-WS (WS-score ws) (WS-mistypes ws) (- (WS-timer ws) 1) (WS-lol ws) (WS-lvl ws))]
      )
    )
  )
       


;draw-letters: (WS->Image)
;Draws the list of letters from WS-lol over a line
(define draw-letters
  (lambda [lol]
    (cond
      [(empty? lol) empty-image]
      [else (beside (text (first lol) 35 "red") (draw-letters (rest lol)))]
      )
    )
  )


;draw-score (WS-Image)
;Draws the score in a box
(define draw-score
  (lambda [b]
      (overlay (above (text "SCORE" 50 "blue") (text (number->string b) 45 "red")) (rectangle 300 100 "outline" "red"))))


;draw-mistypes (WS->Image)
;Draws the number mistakes the user has made
(define draw-mistypes
  (lambda [m]
    (overlay (above (text "MISTYPES" 50 "blue") (text (number->string m) 45 "red")) (rectangle 300 100 "outline" "red"))))


;draw-time (WS->Image)
;Displays the amount of time the player has to finish typing a given display of letters
(define draw-timer
  (lambda [t]
    (above (text "TIME" 50 "green") (overlay (overlay (text (number->string t) 45 "black") (rectangle 200 50 "solid" "white")) (rectangle 300 50 "solid" "green")))
    )
  )

;draw-lvl: (WS->Image)
;Displays the level the player is currently on
(define draw-lvl
  (lambda [l]
    (above (text "LEVEL" 50 "white") (overlay (text (number->string l) 45 "black") (square 50 "solid" "green")))
    )
  )


;draw: (WS->Image)
;Draws the entire worldstate
(define draw
  (lambda [ws]
    (underlay/align "right" "middle"
     (place-images
     (list
           (draw-timer (WS-timer ws))
           (draw-score (WS-score ws))
           (draw-mistypes (WS-mistypes ws))
           (draw-lvl (WS-lvl ws))
           LINE
           )
     (list
           (make-posn (/ WIDTH 2) 50)
           (make-posn (* (/ 1 3) WIDTH) 200)
           (make-posn (* (/ 2 3) WIDTH) 200)
           (make-posn (/ WIDTH 2) 200)
           (make-posn 775 513)
           )
     BACKGROUND)
     (underlay/align "left" "bottom" (beside (draw-letters (WS-lol ws)) empty-image) SHORT-LINE)
     )
     
    )
  )
   
;key-hander: (Key-Input X WS -> WS)
   ;Checks whether the key pressed by the user matches the first of the list of letters given; if it does, it adds to the score and removes it from the list; if it does not match it adds to the mistypes
(define key-handler
  (lambda [ws a-key]
    (cond
      [(key=? a-key (first (WS-lol ws)))
          (make-WS (+ 5 (WS-score ws)) (WS-mistypes ws) (WS-timer ws) (rest (WS-lol ws)) (WS-lvl ws))]
      [(not (key=? a-key (first (WS-lol ws))))
       (make-WS (WS-score ws) (+ (WS-mistypes ws) 1) (WS-timer ws) (WS-lol ws) (WS-lvl ws))]
      [else (make-WS (WS-score ws) (WS-mistypes ws) (WS-timer ws) (WS-lol ws) (WS-lvl ws))]
      )
    )
  )

;game-end: (WS->WS)
   ;Checks to see whether the mistype max has been reached or the timer has run out; if either is true, the game ends with the last picture function.
(define game-end
  (lambda [ws]
    (cond
      [(>= (WS-mistypes ws) MISTYPE-MAX) #true]
      [(equal? (WS-timer ws) 0) #true]
      [else #false]
      )
    )
  )


;last-pictures: (WS->WS)
   ;Displays the score, the level, and (depending on the score) a varying message
(define last-picture
  (lambda [ws]
    (cond
      [(<= (WS-score ws) POOR-SCORE) (overlay (above (text "DO BETTER" 200 "red") (above (draw-score (WS-score ws)) (draw-lvl (WS-lvl ws)))) BACKGROUND)]
      [(<= (WS-score ws) DECENT-SCORE) (overlay (above (text "NOT BAD" 200 "orange") (above (draw-score (WS-score ws)) (draw-lvl (WS-lvl ws)))) BACKGROUND)]
      [(<= (WS-score ws) GOOD-SCORE) (overlay (above (text "PURTY GOOD" 200 "yellow") (above (draw-score (WS-score ws)) (draw-lvl (WS-lvl ws)))) BACKGROUND)]
      [(<= (WS-score ws) EXCELLENT-SCORE)(overlay (above (text "WELL DONE" 200 "green") (above (draw-score (WS-score ws)) (draw-lvl (WS-lvl ws)))) BACKGROUND)]
      [(>= (WS-score ws) EXCELLENT-SCORE)(overlay (above (text "EXCELLENT!!!" 200 "green") (above (draw-score (WS-score ws)) (draw-lvl (WS-lvl ws)))) BACKGROUND)]
      
    )
  )
  )
        


(big-bang init-WS
  (to-draw draw)
  (on-key key-handler)
  (on-tick tock 1)
  (stop-when game-end last-picture)
  )





          
 
  


                              
      
      
                       
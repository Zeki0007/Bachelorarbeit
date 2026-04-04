module State where

data DocumentState
    = Draft String                               -- Just the text
    | Review String                              -- Just the text
    | Published String String String             -- Text, Reviewer, Date
    deriving (Show, Eq)

-- 2. Pure functions with 'Either' for error handling
-- Right = Success (new state), Left = Failure (error message)
edit :: DocumentState -> String -> Either String DocumentState
edit (Draft _) newContent     = Right (Draft newContent)
edit (Review _) _             = Left "A document in review cannot be edited!"
edit (Published _ _ _) _      = Left "A published document is read-only!"

submitForReview :: DocumentState -> Either String DocumentState
submitForReview (Draft content)   = Right (Review content)
submitForReview (Review _)        = Left "A document is already in review!"
submitForReview (Published _ _ _) = Left "Already published!"

approve :: DocumentState -> String -> String -> Either String DocumentState
approve (Draft _) _ _             = Left "A draft cannot be approved directly!"
approve (Review content) rev date = Right (Published content rev date)
approve (Published _ _ _) _ _     = Left "Already published!"

reject :: DocumentState -> Either String DocumentState
reject (Draft _)         = Left "A draft cannot be rejected!"
reject (Review content)  = Right (Draft content)
reject (Published _ _ _) = Left "Already published, cannot be rejected!"
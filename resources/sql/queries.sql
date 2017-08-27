-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
 (registered, email, pass)
VALUES (:registered, :email, :pass)

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE email = :email

/*
-- :name delete-user!
-- :doc delete a user given the id
DELETE FROM users
WHERE email = :email
*/

-- :name get-questions :? :*
-- :doc retrieve a question given topic id
select id,text from questions
where topic = :topic order by id asc


-- :name get-answers :? :*
-- :doc retrieve an answer give question id
select id,text from responses
where id in (select id from qu_re_connection
      	     where question = :question)
	     order by id asc

-- :name get-response :? :1
-- :doc get response for given id
select * from responses where id = :id


-- :name calculate-points :? :*
-- :doc calc points from user responses
select points from responses where id in (:v*:ids)

-- :name create-user-response :! :n
-- :doc creates user-response
insert into user_responses
(user, topic, points, date)
values (:user, :topic, :points, :date)

-- :name get-topic-name :? :1
-- :doc returns topic name for given id
select name from topics where id = :topic


-- :name get-results :? :*
-- :doc returns results for given email
select topic,points, max(date)
from user_responses group by topic order by max(date)


/*
-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

*/

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
(email, topic, points, submitted)
values (:email, :topic, :points, :submitted)

-- :name get-topic-name :? :1
-- :doc returns topic name for given id
select name from topics where id = :topic


-- :name get-results :? :*
-- :doc returns results for given email
select topic, points, submitted from user_responses where email = :email order by submitted desc
-- select topic,points, max(date)
-- from user_responses where user = :email group by topic order by max(date)

-- :name topics-user-results :? :*
-- :docs returns distinct topics from user responses
select distinct topic from user_responses where email = :email and topic < 7;

---- ovo je resenje da hugsql povezuje pitanja i odgovore, moze se desiti da je efikasnije od onoga sto vec imam. neka bude opcija pa cemo videti posle
-- -- :name get-q&r :? :*
-- -- :doc returns questions and answers
-- Select questions.id, questions.text, group_concat(responses.id), group_concat(responses.text)
-- from questions
-- join qu_re_connection on questions.id = qu_re_connection.question
-- join responses on responses.id = qu_re_connection.response
-- group by questions.id

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

--
-- CREATE TABLE USERS
--

CREATE TABLE public.users
(
    id           bigint PRIMARY KEY,
    active       boolean                NOT NULL DEFAULT FALSE,
    email        character varying(512) NOT NULL UNIQUE CONSTRAINT email_regexp CHECK ( email ~ '^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    generated_at timestamp(6) without time zone,
    mailing_type smallint               NOT NULL,
    name         character varying(150) NOT NULL,
    password     character varying(255) NOT NULL,
    role         smallint               NOT NULL,
    sent_emails  bigint                 NOT NULL DEFAULT 0,
    updated_at   timestamp(6) without time zone,
    username     character varying(30)  NOT NULL UNIQUE
);



--
-- SEQUENCE ON PRIMARY KEY
--

CREATE SEQUENCE public.users_id_seq START WITH 1 INCREMENT BY 1 MINVALUE 1 NO MAXVALUE CACHE 1;



--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;



--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);



--
-- FUNCTION TO UPDATE TIME OF COLUMN updated_at
--

CREATE FUNCTION public.update_time() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.updated_at
        = (select now());
    RETURN NEW;
END
$$;



--
-- CALLS FUNCTION update_time() for every INSERT OR UPDATE on TABLE USERS
--

CREATE TRIGGER update_time
    BEFORE INSERT OR
        UPDATE
    ON public.users
    FOR EACH ROW
EXECUTE FUNCTION public.update_time();



--
-- FUNCTION TO UPDATE TIME OF COLUMN generated_at
--

CREATE FUNCTION public.generate_time() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.generated_at
        = (select now());
    RETURN NEW;
END
$$;



--
-- CALLS FUNCTION generate_time() for every INSERT OR UPDATE on TABLE USERS
--

CREATE TRIGGER generate_time
    BEFORE INSERT
    ON public.users
    FOR EACH ROW
EXECUTE FUNCTION public.generate_time();

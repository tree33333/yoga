package org.skyscreamer.yoga.demo.resteasy.resources;

import org.skyscreamer.yoga.demo.model.User;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Carter Page
 */
@Controller
@Path("/user")
public class UserController extends AbstractController<User>
{
    @GET
    public List<User> getUsers( @QueryParam("selector") String selectorString )
    {
        return _genericDao.findAll( User.class );
    }
}

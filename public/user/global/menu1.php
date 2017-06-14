<?php debug_backtrace() || die ("<h2>Error 405: Method Not Allowed</h2>
<p>Direct access to this url is forbidden.</p>
<hr>
<i>Powered by</i> <a href='http://www.dezignburg.com'> Dezign Burg</a><i> web services.</i>"); ?>
        <ul>
          <li class="<?php if($page=='send'){echo 'active';}?>">
            <span class="current-arrow"></span>
            <a href="/user/send">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe067;"></span>
              </div>
              Send Money
            </a>
          </li>
          <li class="<?php if($page=='transactions'){echo 'active';}?>">
            <a href="/user/transactions">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe047;"></span>
              </div>
              Transactions
            </a>
          </li>
          <li class="<?php if($page=='verification'){echo 'active';}?>">
            <a href="/user/verification">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe0fe;"></span>
              </div>
              Verification
            </a>
          </li>
          <li class="<?php if($page=='account'){echo 'active';}?>">
            <a href="/user/account">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe08e;"></span>
              </div>
              Settings
            </a>
          </li>          
        </ul>
